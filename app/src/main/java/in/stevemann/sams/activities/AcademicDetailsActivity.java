package in.stevemann.sams.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.stevemann.sams.R;
import in.stevemann.sams.models.AcademicModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class AcademicDetailsActivity extends AppCompatActivity {

    private AcademicModel academicModel = null;
    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    @BindView(R.id.academic_details_student_name)
    TextView _studentName;
    @BindView(R.id.academic_details_batch)
    TextView _batch;
    @BindView(R.id.academic_details_category)
    TextView _category;
    @BindView(R.id.academic_details_programme)
    TextView _programme;
    @BindView(R.id.academic_details_roll_no)
    TextView _rollNo;
    @BindView(R.id.btn_delete_academic_achievement)
    Button _deleteAchievementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_details);
        ButterKnife.bind(this);
        academicModel = Objects.requireNonNull(getIntent().getExtras()).getParcelable("academicObj");

        if (TokenUtil.dataExists(this)) {
            _deleteAchievementButton.setEnabled(true);
            _deleteAchievementButton.setVisibility(View.VISIBLE);

            _deleteAchievementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = new ProgressDialog(v.getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Deleting Academic Achievement...");
                    progressDialog.show();

                    String encryptedData = TokenUtil.readData(getApplicationContext());
                    String[] data = encryptedData.split(" ");
                    String encryptedToken = data[1];
                    String iv = data[0];
                    String token = cryptoUtil.decryptToken(encryptedToken, iv);

                    RequestParams params = new RequestParams();
                    params.put("token", token);
                    params.put("id", academicModel.getId());

                    RESTClient.post("academic/delete", params, new JsonHttpResponseHandler() {
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
                                            onDeleteSuccess();
                                            progressDialog.dismiss();
                                        }, 3000);
                            } else {
                                new android.os.Handler().postDelayed(() -> {
                                            onDeleteFailed();
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
            });
        } else {
            _deleteAchievementButton.setVisibility(View.GONE);
        }

        _batch.setText(academicModel.getBatch());
        _studentName.setText(academicModel.getName());
        _category.setText(academicModel.getCategory());
        _programme.setText(academicModel.getProgramme());
        _rollNo.setText(academicModel.getRollNo());
    }

    private void onDeleteSuccess() {
        _deleteAchievementButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Academic Achievement Deletion Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void onDeleteFailed() {
        Toast.makeText(getBaseContext(), "Academic Achievement Deletion failed", Toast.LENGTH_LONG).show();
        _deleteAchievementButton.setEnabled(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
