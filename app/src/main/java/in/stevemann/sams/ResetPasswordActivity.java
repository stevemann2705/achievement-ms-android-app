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
import in.stevemann.sams.utils.CommonUtils;
import in.stevemann.sams.utils.RESTClient;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    RESTClient client = new RESTClient();

    @BindView(R.id.input_old_password)
    EditText _oldPasswordText;
    @BindView(R.id.input_new_password)
    EditText _newPasswordText;
    @BindView(R.id.input_reEnter_new_password)
    EditText _reEnterNewPasswordText;
    @BindView(R.id.btn_reset_password)
    Button _resetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        _resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    public void resetPassword() {
        String oldPassword = _oldPasswordText.getText().toString();
        String newPassword = _newPasswordText.getText().toString();

        if (!validate()) {
            onResetFailed("Unable to validate passwords");
            return;
        }

        _resetButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Resetting Password...");
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.put("email", CommonUtils.email);
        params.put("currentpass", oldPassword);
        params.put("newpass", newPassword);

        RESTClient.post("users/resetpass", params, new JsonHttpResponseHandler() {
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
                progressDialog.dismiss();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onResetSuccess() {
        Toast.makeText(getBaseContext(), "Password Reset Successful", Toast.LENGTH_LONG).show();
        _resetButton.setEnabled(true);
        finish();
    }

    public void onResetFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _resetButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String oldPassword = _oldPasswordText.getText().toString();
        String newPassword = _newPasswordText.getText().toString();
        String reEnterNewPassword = _reEnterNewPasswordText.getText().toString();

        if (oldPassword.isEmpty() || oldPassword.length() < 4 || oldPassword.length() > 10) {
            _oldPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _oldPasswordText.setError(null);
        }

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            _newPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _newPasswordText.setError(null);
        }

        if (reEnterNewPassword.isEmpty() || reEnterNewPassword.length() < 4 || reEnterNewPassword.length() > 10) {
            _reEnterNewPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _reEnterNewPasswordText.setError(null);
        }

        if(!reEnterNewPassword.equals(newPassword)){
            _reEnterNewPasswordText.setError("Passwords don't match");
            valid = false;
        } else {
            _reEnterNewPasswordText.setError(null);
        }

        return valid;
    }
}
