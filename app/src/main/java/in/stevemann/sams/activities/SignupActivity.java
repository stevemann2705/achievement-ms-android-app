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
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.SpinnerUtil;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_firstname)
    EditText _firstNameText;
    @BindView(R.id.input_lastname)
    EditText _lastNameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_department)
    Spinner _department;
    @BindView(R.id.input_shift)
    Spinner _shift;
    @BindView(R.id.input_code)
    EditText _code;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.input_designation)
    EditText _designation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(v -> signup());

        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });

        String[] departments = {"computerscience", "education", "management"};
        ArrayAdapter<String> departmentAdapter = SpinnerUtil.getAdapter(this, departments, "Select Department");
        SpinnerUtil.setSpinnerProperties(_department, departmentAdapter);
        SpinnerUtil.setTextColorOnItemSelected(_department, Color.WHITE);

        String[] shifts = {"morning", "evening"};
        ArrayAdapter<String> shiftAdapter = SpinnerUtil.getAdapter(this, shifts, "Select Shift");
        SpinnerUtil.setSpinnerProperties(_shift, shiftAdapter);
        SpinnerUtil.setTextColorOnItemSelected(_shift, Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String department = _department.getSelectedItem().toString();
        String shift = _shift.getSelectedItem().toString();
        String password = _passwordText.getText().toString();
        String code = _code.getText().toString();
        String designation = _designation.getText().toString();

        RequestParams params = new RequestParams();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("department", department);
        params.put("shift", shift);
        params.put("email", email);
        params.put("password", password);
        params.put("code", code);
        params.put("designation", designation);

        RESTClient.post("users/add", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                boolean response = false;
                try {
                    response = (boolean) timeline.get("bool");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response) {
                    new android.os.Handler().postDelayed(() -> {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }, 3000);
                } else {
                    new android.os.Handler().postDelayed(() -> {
                        onSignupFailed();
                        progressDialog.dismiss();
                    }, 3000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });

    }


    private void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    private boolean validate() {
        return isValidFirstName() && isValidLastName() && isValidEmail() && isValidDepartment() &&
                isValidShift() && isValidPassword() && isValidRePassword();
    }

    private boolean isValidFirstName() {
        String firstName = _firstNameText.getText().toString();
        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            return false;
        } else {
            _firstNameText.setError(null);
            return true;
        }
    }

    private boolean isValidLastName() {
        String lastName = _lastNameText.getText().toString();
        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            return false;
        } else {
            _lastNameText.setError(null);
            return true;
        }
    }

    private boolean isValidEmail() {
        String email = _emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            return false;
        } else {
            _emailText.setError(null);
            return true;
        }
    }

    private boolean isValidDepartment() {
        String department = _department.getSelectedItem().toString();
        if ("Select Department".equals(department)) {
            TextView errorText = (TextView) _department.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select department");
            return false;
        } else {
            System.out.println("No Error in Department Spinner");
            return true;
        }
    }

    private boolean isValidShift() {
        String shift = _shift.getSelectedItem().toString();
        if ("Select Shift".equals(shift)) {
            TextView errorText = (TextView) _shift.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select shift");
            return false;
        } else {
            System.out.println("No Error in Shift Spinner");
            return true;
        }
    }

    private boolean isValidPassword() {
        String password = _passwordText.getText().toString();
        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            _passwordText.setError("between 6 and 20 alphanumeric characters");
            return false;
        } else {
            _passwordText.setError(null);
            return true;
        }
    }

    private boolean isValidRePassword() {
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String password = _passwordText.getText().toString();
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 20 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password don't match");
            return false;
        } else {
            _reEnterPasswordText.setError(null);
            return true;
        }
    }
}