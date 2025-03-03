package in.stevemann.sams.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.stevemann.sams.R;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.SpinnerUtil;

public class AddAchievementActivity extends AppCompatActivity {
    private static final String TAG = "AddAchievementActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_rollNo)
    EditText _rollNoText;
    @BindView(R.id.input_section)
    Spinner _section;
    @BindView(R.id.input_sessionFrom)
    EditText _sessionFromText;
    @BindView(R.id.input_sessionTo)
    EditText _sessionToText;
    @BindView(R.id.input_semester)
    Spinner _semester;
    @BindView(R.id.input_department)
    Spinner _department;
    @BindView(R.id.input_shift)
    Spinner _shift;
    @BindView(R.id.input_eventName)
    EditText _eventNameText;
    @BindView(R.id.input_eventDate)
    EditText _eventDateText;
    @BindView(R.id.input_titleAwarded)
    EditText _titleAwardedText;
    @BindView(R.id.input_eventVenue)
    EditText _eventVenueText;
    @BindView(R.id.input_category)
    Spinner _category;
    @BindView(R.id.input_eventDescription)
    EditText _eventDescriptionText;
    @BindView(R.id.checkbox_participated)
    CheckBox _participatedCheckBox;
    @BindView(R.id.checkbox_organized)
    CheckBox _organisedCheckBox;
    @BindView(R.id.btn_image)
    Button _imageButton;
    @BindView(R.id.btn_submit)
    Button _submitButton;

    private static final int RESULT_LOAD_IMAGE = 1;

    private boolean booleanParticipated = true;
    private String imageGot = null;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievement);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }

        _submitButton.setOnClickListener(v -> submit());

        String[] departments = {"computerscience", "education", "management"};
        ArrayAdapter<String> departmentAdapter = SpinnerUtil.getAdapter(this, departments, "Select Department");
        SpinnerUtil.setSpinnerProperties(_department, departmentAdapter);

        String[] sections = {"A", "B", "C", "D"};
        ArrayAdapter<String> sectionAdapter = SpinnerUtil.getAdapter(this, sections, "Select Section");
        SpinnerUtil.setSpinnerProperties(_section, sectionAdapter);

        String[] semesters = {"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> semesterAdapter = SpinnerUtil.getAdapter(this, semesters, "Select Semester");
        SpinnerUtil.setSpinnerProperties(_semester, semesterAdapter);

        String[] shifts = {"morning", "evening"};
        ArrayAdapter<String> shiftAdapter = SpinnerUtil.getAdapter(this, shifts, "Select Shift");
        SpinnerUtil.setSpinnerProperties(_shift, shiftAdapter);

        String[] categories = {"sports", "technical", "cultural", "others"};
        ArrayAdapter<String> categoriesAdapter = SpinnerUtil.getAdapter(this, categories, "Select Category");
        SpinnerUtil.setSpinnerProperties(_category, categoriesAdapter);

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
                    _sessionFromText.setText(batch[0]);
                    _sessionToText.setText(batch[1]);
                }
            }
        });
    }

    private void submit() {
        Log.d(TAG, "Submit");

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddAchievementActivity.this,
                R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Data...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String rollNo = _rollNoText.getText().toString();
        String section = _section.getSelectedItem().toString();
        String sessionFrom = _sessionFromText.getText().toString();
        String sessionTo = _sessionToText.getText().toString();
        String semester = _semester.getSelectedItem().toString();
        String department = _department.getSelectedItem().toString();
        String shift = _shift.getSelectedItem().toString();
        String eventName = _eventNameText.getText().toString();
        String eventDate = _eventDateText.getText().toString();
        String titleAwarded = _titleAwardedText.getText().toString();
        String eventVenue = _eventVenueText.getText().toString();
        String category = _category.getSelectedItem().toString();
        String eventDescription = _eventDescriptionText.getText().toString();
        boolean participated = booleanParticipated;

        RequestParams params = new RequestParams();
        params.put("title", titleAwarded);
        params.put("rollNo", rollNo);
        params.put("department", department);
        params.put("semester", semester);
        params.put("date", eventDate);
        params.put("shift", shift);
        params.put("section", section);
        params.put("sessionFrom", sessionFrom);
        params.put("sessionTo", sessionTo);
        params.put("venue", eventVenue);
        params.put("category", category);
        params.put("participated", String.valueOf(participated));
        params.put("name", name);
        params.put("description", eventDescription);
        params.put("eventName", eventName);

        File myFile = new File(imageGot);

        try {
            params.put("image", myFile, "image/jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        RESTClient.post("achievements/add", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {

                boolean response = true;
                try {
                    response = (boolean) timeline.get("bool");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response) {
                    new android.os.Handler().postDelayed(() -> {
                                onSubmitSuccess();
                                progressDialog.dismiss();
                            }, 3000);
                } else {
                    new android.os.Handler().postDelayed(() -> {
                                onSubmitFailed();
                                progressDialog.dismiss();
                            }, 3000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });

    }

    private void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Achievement Submission Successful", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Achievement Submission failed", Toast.LENGTH_LONG).show();
        _submitButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String rollNo = _rollNoText.getText().toString();
        String section = _section.getSelectedItem().toString();
        String sessionFrom = _sessionFromText.getText().toString();
        String sessionTo = _sessionToText.getText().toString();
        String department = _department.getSelectedItem().toString();
        String eventName = _eventNameText.getText().toString();
        String eventDate = _eventDateText.getText().toString();
        String titleAwarded = _titleAwardedText.getText().toString();
        String eventVenue = _eventVenueText.getText().toString();
        String eventDescription = _eventDescriptionText.getText().toString();

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

        if ("Select Section".equals(section)) {
            TextView errorText = (TextView) _section.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select section");
            valid = false;
        } else {
            Log.d("SECTION SPINNER: ","No Error in Section Spinner");
        }

        if ("Select Semester".equals(section)) {
            TextView errorText = (TextView) _semester.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select semester");
            valid = false;
        } else {
            Log.d("SEMESTER SPINNER: ","No Error in Semester Spinner");
        }

        if ("Select Shift".equals(section)) {
            TextView errorText = (TextView) _shift.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select shift");
            valid = false;
        } else {
            Log.d("SHIFT SPINNER: ","No Error in Shift Spinner");
        }

        if ("Select Category".equals(section)) {
            TextView errorText = (TextView) _category.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select category");
            valid = false;
        } else {
            Log.d("CATEGORY SPINNER: ","No Error in Category Spinner");
        }

        if ("Department".equals(department)) {
            TextView errorText = (TextView) _department.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select department");
            valid = false;
        } else {
            Log.d("DEPARTMENT SPINNER: ","No Error in Department Spinner");
        }

        if (sessionFrom.isEmpty() || sessionFrom.length() != 4) {
            _sessionFromText.setError("Session year (FORMAT: YYYY)");
            valid = false;
        } else {
            _sessionFromText.setError(null);
        }

        if (sessionTo.isEmpty() || sessionTo.length() != 4) {
            _sessionToText.setError("Session year (FORMAT: YYYY)");
            valid = false;
        } else {
            _sessionToText.setError(null);
        }

        if (eventName.isEmpty() || eventName.length() < 3) {
            _eventNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _eventNameText.setError(null);
        }

        // TODO: Event Date Validation
        if (eventDate.isEmpty() || eventDate.length() < 3) {
            _eventDateText.setError("at least 3 characters");
            valid = false;
        } else {
            _eventDateText.setError(null);
        }

        if (titleAwarded.isEmpty() || titleAwarded.length() < 3) {
            _titleAwardedText.setError("at least 3 characters");
            valid = false;
        } else {
            _titleAwardedText.setError(null);
        }

        if (eventDescription.isEmpty() || eventDescription.length() < 3) {
            _eventDescriptionText.setError("at least 3 characters");
            valid = false;
        } else {
            _eventDescriptionText.setError(null);
        }

        if (eventVenue.isEmpty() || eventVenue.length() < 3) {
            _eventVenueText.setError("at least 3 characters");
            valid = false;
        } else {
            _eventVenueText.setError(null);
        }

        // TODO: Verify Participated Validation
        if((!_participatedCheckBox.isChecked() && !_organisedCheckBox.isChecked()) || (_participatedCheckBox.isChecked() && _organisedCheckBox.isChecked())){
            valid = false;
            _participatedCheckBox.setError("Please check one checkbox");
            _organisedCheckBox.setError("Please check one checkbox");
        }

        if(imageGot == null || imageGot.isEmpty()){
            valid = false;
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            _imageButton.setBackgroundColor(Color.RED);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageGot = cursor.getString(columnIndex);
            System.out.println(imageGot);
            cursor.close();

        }
    }

    public void getImageString(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_participated:
                if (checked)
                    booleanParticipated = true;
                    _organisedCheckBox.setChecked(false);
                    break;
            case R.id.checkbox_organized:
                if (checked)
                    booleanParticipated = false;
                    _participatedCheckBox.setChecked(false);
                    break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddAchievementActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddAchievementActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddAchievementActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddAchievementActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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