package in.stevemann.sams.activities;

import android.app.ProgressDialog;
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
import in.stevemann.sams.models.TeacherAchievementModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class TeacherAchievementDetailsActivity extends AppCompatActivity {

    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();
    @BindView(R.id.teacher_achievement_details_userid)
    TextView _userid;
    @BindView(R.id.teacher_achievement_details_date)
    TextView _date;
    @BindView(R.id.teacher_achievement_details_description)
    TextView _description;
    @BindView(R.id.teacher_achievement_details_international)
    TextView _international;
    @BindView(R.id.teacher_achievement_details_msi)
    TextView _msi;
    @BindView(R.id.teacher_achievement_details_place)
    TextView _place;
    @BindView(R.id.teacher_achievement_details_published)
    TextView _published;
    @BindView(R.id.teacher_achievement_details_reviewed)
    TextView _reviewed;
    @BindView(R.id.teacher_achievement_details_sponsored)
    TextView _sponsored;
    @BindView(R.id.teacher_achievement_details_subtype)
    TextView _subtype;
    @BindView(R.id.teacher_achievement_details_type)
    TextView _type;
    @BindView(R.id.teacher_achievement_details_topic)
    TextView _topic;
    @BindView(R.id.btn_delete_teacher_achievement)
    Button _deleteTeacherAchievementButton;
    private TeacherAchievementModel achievementModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_achievement_details);
        ButterKnife.bind(this);

        achievementModel = Objects.requireNonNull(getIntent().getExtras().getParcelable("teacherAchievementObject"));

        if (TokenUtil.dataExists(this)) {
            _deleteTeacherAchievementButton.setEnabled(true);
            _deleteTeacherAchievementButton.setVisibility(View.VISIBLE);

            _deleteTeacherAchievementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = new ProgressDialog(v.getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Deleting Teacher Achievement...");
                    progressDialog.show();

                    String encryptedData = TokenUtil.readData(getApplicationContext());
                    String[] data = encryptedData.split(" ");
                    String encryptedToken = data[1];
                    String iv = data[0];
                    String token = cryptoUtil.decryptToken(encryptedToken, iv);

                    RequestParams params = new RequestParams();
                    params.put("token", token);
                    params.put("id", achievementModel.getId());

                    RESTClient.post("tachievements/delete", params, new JsonHttpResponseHandler() {
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
                                                onDeleteSuccess();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            } else {
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                onDeleteFailed();
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
            });
        } else {
            _deleteTeacherAchievementButton.setVisibility(View.GONE);
        }

        _date.setText(achievementModel.getDate());
        _description.setText(achievementModel.getDescription());
        _international.setText(String.valueOf(achievementModel.isInternational()));
        _msi.setText(String.valueOf(achievementModel.isMsi()));
        _place.setText(achievementModel.getPlace());
        _published.setText(achievementModel.getPublished());
        _reviewed.setText(String.valueOf(achievementModel.isReviewed()));
        _sponsored.setText(String.valueOf(achievementModel.isSponsored()));
        _subtype.setText(achievementModel.getSubType());
        _topic.setText(achievementModel.getTopic());
        _type.setText(achievementModel.getTaType());
        _userid.setText(achievementModel.getId());
    }

    private void onDeleteSuccess() {
        _deleteTeacherAchievementButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Teacher Achievement Deletion Successful", Toast.LENGTH_LONG).show();
        finish();
    }

    private void onDeleteFailed() {
        Toast.makeText(getBaseContext(), "Teacher Achievement Deletion failed", Toast.LENGTH_LONG).show();
        _deleteTeacherAchievementButton.setEnabled(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
