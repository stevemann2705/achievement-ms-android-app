package in.stevemann.sams;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ResetProfileActivity extends AppCompatActivity {
    private static final String TAG = "ResetProfileActivity";

    RESTClient client = new RESTClient();

    @BindView(R.id.input_current_email)
    EditText _currentEmailText;
    @BindView(R.id.input_new_email)
    EditText _newEmailText;
    @BindView(R.id.input_reset_firstname)
    EditText _resetFirstNameText;
    @BindView(R.id.input_reset_lastname)
    EditText _resetLastNameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_reset_profile)
    Button _resetProfileButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_profile);
        ButterKnife.bind(this);

        _resetProfileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetProfile();
            }
        });
    }

    public void resetProfile() {

        String currentEmail = _currentEmailText.getText().toString();
        String newEmail = _newEmailText.getText().toString();
        String firstName = _resetFirstNameText.getText().toString();
        String lastName = _resetLastNameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!validate()) {
            onResetFailed("Unable to validate inputs");
            return;
        }

        _resetProfileButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ResetProfileActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Resetting Password...");
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.put("email", currentEmail);
        params.put("newEmail", newEmail);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("password", password);

        client.post("users/reset", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                boolean response = false;
                String message = null;
                try {
                    response = (boolean) timeline.get("bool");
                    message = timeline.get("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String finalMessage = message;
                if (response) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onResetSuccess();
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
                                    onResetFailed(finalMessage);
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("LOGIN AUTH RESPONSE", "Communication with server failed. Try again.");
                // TODO: Handle onFailure() for login
            }
        });

    }

    public void onResetSuccess() {
        Toast.makeText(getBaseContext(), "Profile Reset Successful", Toast.LENGTH_LONG).show();
        _resetProfileButton.setEnabled(true);
        finish();
    }

    public void onResetFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _resetProfileButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String currentEmail = _currentEmailText.getText().toString();
        String newEmail = _newEmailText.getText().toString();
        String firstName = _resetFirstNameText.getText().toString();
        String lastName = _resetLastNameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _resetFirstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _resetFirstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _resetLastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _resetLastNameText.setError(null);
        }

        if (currentEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _currentEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            _currentEmailText.setError(null);
        }
        if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            _newEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            _newEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
