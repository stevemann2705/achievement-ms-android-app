package in.stevemann.sams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.SpinnerUtil;
import in.stevemann.sams.utils.TokenUtil;

public class AddAcademicAchievementActivity extends AppCompatActivity {
    private static final String TAG = "AddAcademicAchievement";

    CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    @BindView(R.id.input_academic_achievement_name)
    EditText _nameText;
    @BindView(R.id.input_academic_achievement_rollNo)
    EditText _rollNoText;
    @BindView(R.id.input_academic_achievement_batch_start_year)
    EditText _batchStartYearText;
    @BindView(R.id.input_academic_achievement_batch_end_year)
    EditText _batchEndYearText;
    @BindView(R.id.input_academic_achievement_category)
    Spinner _category;
    @BindView(R.id.input_academic_achievement_programme)
    Spinner _programme;
    @BindView(R.id.btn_submit_academic_achievement)
    Button _submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_academic_achievement);
        ButterKnife.bind(this);

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        String[] categories = {"goldmedalist", "exemplary", "both"};
        ArrayAdapter<String> categoriesAdapter = SpinnerUtil.getAdapter(this, categories, "Select Category");
        SpinnerUtil.setSpinnerProperties(_category, categoriesAdapter);

        String[] programmes = {"B. Ed.", "BBA (H) 4 years", "BBA (General)", "BBA (B&I)", "BBA (T&TM)", "BCA", "B.Com (H)"};
        ArrayAdapter<String> programmesAdapter = SpinnerUtil.getAdapter(this, programmes, "Select Programme");
        SpinnerUtil.setSpinnerProperties(_programme, programmesAdapter);

        _rollNoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (_rollNoText.getText().toString().length() >= 11) {
                    String[] batch = getBatch(_rollNoText.getText().toString());
                    _batchStartYearText.setText(batch[0]);
                    _batchEndYearText.setText(batch[1]);
                }
            }
        });
    }

    public void submit() {
        Log.d(TAG, "Submit");

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddAcademicAchievementActivity.this,
                R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Data...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String rollNo = _rollNoText.getText().toString();
        String batchStartYear = _batchStartYearText.getText().toString();
        String batchEndYear = _batchEndYearText.getText().toString();
        String category = _category.getSelectedItem().toString();
        String programme = _programme.getSelectedItem().toString();

        String encryptedData = TokenUtil.readData(getApplicationContext());
        String[] data = encryptedData.split(" ");
        String encryptedToken = data[1];
        String iv = data[0];

        String token = cryptoUtil.decryptToken(encryptedToken, iv);

        RequestParams params = new RequestParams();
        params.put("rollNo", rollNo);
        params.put("name", name);
        params.put("batch", batchStartYear);
        params.put("programme", programme);
        params.put("token", token);
        params.put("category", category);

        RESTClient.post("academic/add", params, new JsonHttpResponseHandler() {
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
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onSubmitSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    onSubmitFailed();
                                    progressDialog.dismiss();
                                }
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

    public void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Academic Achievement Submission Successful", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Academic Achievement Submission failed", Toast.LENGTH_LONG).show();
        _submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String rollNo = _rollNoText.getText().toString();
        String batchStartYear = _batchStartYearText.getText().toString();
        String batchEndYear = _batchEndYearText.getText().toString();
        String category = _category.getSelectedItem().toString();
        String programme = _programme.getSelectedItem().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (rollNo.isEmpty() || rollNo.length() < 3) {
            _rollNoText.setError("at least 3 characters");
            valid = false;
        } else {
            _rollNoText.setError(null);
        }

        if (batchStartYear.isEmpty() || batchStartYear.length() != 4) {
            _batchStartYearText.setError("Session year (FORMAT: YYYY)");
            valid = false;
        } else {
            _batchStartYearText.setError(null);
        }

        if (batchEndYear.isEmpty() || batchEndYear.length() != 4) {
            _batchEndYearText.setError("Session year (FORMAT: YYYY)");
            valid = false;
        } else {
            _batchEndYearText.setError(null);
        }

        if ("Select Category".equals(category)) {
            TextView errorText = (TextView) _category.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select category");
            valid = false;
        } else {
            Log.d("Category SPINNER: ", "No Error in Semester Spinner");
        }

        if ("Select Programme".equals(programme)) {
            TextView errorText = (TextView) _programme.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select programme");
            valid = false;
        } else {
            Log.d("Programme SPINNER: ", "No Error in Semester Spinner");
        }

        return valid;
    }

    private String[] getBatch(String _rollNo) {
        String temp = _rollNo.substring(Math.max(_rollNo.length() - 5, 0));
        String startYear = temp.substring(Math.max(temp.length() - 2, 0));
        String programme = temp.substring(0, 3);
        String endYear;

        if ("021".equals(programme)) {
            endYear = String.valueOf(Integer.parseInt(startYear) + 2);
        } else {
            endYear = String.valueOf(Integer.parseInt(startYear) + 3);
        }

        startYear = "20" + startYear;
        endYear = "20" + endYear;
        return new String[]{startYear, endYear};
    }
}
