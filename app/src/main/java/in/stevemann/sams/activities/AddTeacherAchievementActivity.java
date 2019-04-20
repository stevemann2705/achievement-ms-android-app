package in.stevemann.sams.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.stevemann.sams.R;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.SpinnerUtil;
import in.stevemann.sams.utils.TokenUtil;

public class AddTeacherAchievementActivity extends AppCompatActivity {
    private static final String TAG = "AddTeacherAchievement";

    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    @BindView(R.id.input_topic)
    EditText _topicText;
    @BindView(R.id.input_description)
    EditText _descriptionText;
    @BindView(R.id.input_publised)
    EditText _publishedText;
    @BindView(R.id.input_date)
    EditText _dateText;
    @BindView(R.id.input_place)
    EditText _placeText;
    @BindView(R.id.input_type)
    Spinner _type;
    @BindView(R.id.input_subtype)
    Spinner _subType;
    @BindView(R.id.checkbox_international)
    CheckBox _internationalCheckBox;
    @BindView(R.id.checkbox_sponsored)
    CheckBox _sponsoredCheckBox;
    @BindView(R.id.checkbox_reviewed)
    CheckBox _reviewedCheckBox;
    @BindView(R.id.checkbox_msi)
    CheckBox _msiCheckBox;
    @BindView(R.id.btn_submit)
    Button _submitButton;

    private boolean booleanInternational;
    private boolean booleanSponsored;
    private boolean booleanReviewed;
    private boolean booleanMsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher_achievement);
        ButterKnife.bind(this);

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        String[] types = {"Book", "Journal", "Conference", "SeminarAttended"};
        ArrayAdapter<String> typeAdapter = SpinnerUtil.getAdapter(this, types, "Select Type");
        SpinnerUtil.setSpinnerProperties(_type, typeAdapter);

        String[] subTypes = {"SEMINAR", "CONFERENCE", "WORKSHOP", "FDP", "FDP1WEEK"};
        ArrayAdapter<String> subTypeAdapter = SpinnerUtil.getAdapter(this, subTypes, "Select Sub Type");
        SpinnerUtil.setSpinnerProperties(_subType, subTypeAdapter);
    }

    private void submit() {
        Log.d(TAG, "Submit");

        String encryptedData = TokenUtil.readData(this);
        String[] data = encryptedData.split(" ");
        String encryptedToken = data[1];
        String iv = data[0];

        String token = cryptoUtil.decryptToken(encryptedToken, iv);

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddTeacherAchievementActivity.this,
                R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Data...");
        progressDialog.show();

        String topic = _topicText.getText().toString();
        String description = _descriptionText.getText().toString();
        String published = _publishedText.getText().toString();
        String date = _dateText.getText().toString();
        String place = _placeText.getText().toString();
        String type = _type.getSelectedItem().toString();
        String subType = _subType.getSelectedItem().toString();
        String international = String.valueOf(booleanInternational);
        String sponsored = String.valueOf(booleanSponsored);
        String reviewed = String.valueOf(booleanReviewed);
        String msi = String.valueOf(booleanMsi);

        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("topic", topic);
        params.put("taType", type);
        params.put("subType", subType);
        params.put("international", international);
        params.put("published", published);
        params.put("sponsored", sponsored);
        params.put("reviewed", reviewed);
        params.put("date", date);
        params.put("description", description);
        params.put("msi", msi);
        params.put("place", place);

        RESTClient.post("tachievements/add", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {

                boolean response = true;
                try {
                    response = (boolean) timeline.get("bool");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response) {
                    new android.os.Handler().postDelayed(
                            () -> {
                                // On complete call either onLoginSuccess or onLoginFailed
                                onSubmitSuccess();
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }, 3000);
                } else {
                    new android.os.Handler().postDelayed(
                            () -> {
                                // On complete call either onLoginSuccess or onLoginFailed
                                //onLoginSuccess();
                                onSubmitFailed();
                                progressDialog.dismiss();
                            }, 3000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });

    }

    private void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Teacher Achievement Submission Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TeacherAchievementActivity.class);
        startActivity(intent);
        finish();
    }

    private void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Teacher Achievement Submission failed", Toast.LENGTH_LONG).show();
        _submitButton.setEnabled(true);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_international:
                if (checked)
                    booleanInternational = true;
                break;
            case R.id.checkbox_sponsored:
                if (checked)
                    booleanSponsored = true;
                break;
            case R.id.checkbox_reviewed:
                if (checked)
                    booleanReviewed = true;
                break;
            case R.id.checkbox_msi:
                if (checked)
                    booleanMsi = true;
                break;
        }
    }

    private boolean validate() {
        boolean valid = true;

        String topic = _topicText.getText().toString();
        String description = _descriptionText.getText().toString();
        String published = _publishedText.getText().toString();
        String date = _dateText.getText().toString();
        String place = _placeText.getText().toString();
        String type = _type.getSelectedItem().toString();
        String subType = _subType.getSelectedItem().toString();
        String international = String.valueOf(booleanInternational);
        String sponsored = String.valueOf(booleanSponsored);
        String reviewed = String.valueOf(booleanReviewed);
        String msi = String.valueOf(booleanMsi);

        if (topic.isEmpty() || topic.length() < 3) {
            _topicText.setError("at least 3 characters");
            valid = false;
        } else {
            _topicText.setError(null);
        }

        if (description.isEmpty() || description.length() < 3) {
            _descriptionText.setError("at least 3 characters");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        if (date.isEmpty() || date.length() < 3) {
            _dateText.setError("at least 3 characters");
            valid = false;
        } else {
            _dateText.setError(null);
        }

        if (place.isEmpty() || place.length() < 3) {
            _placeText.setError("at least 3 characters");
            valid = false;
        } else {
            _placeText.setError(null);
        }

        if ("Select Type".equals(type)) {
            TextView errorText = (TextView) _type.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select type");
            valid = false;
        } else {
            Log.d("TYPE SPINNER: ", "No Error in Type Spinner");
        }

        if ("Select Sub Type".equals(subType)) {
            TextView errorText = (TextView) _subType.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select sub type");
            valid = false;
        } else {
            Log.d("SUBTYPE SPINNER: ", "No Error in Sub Type Spinner");
        }

        return valid;
    }
}
