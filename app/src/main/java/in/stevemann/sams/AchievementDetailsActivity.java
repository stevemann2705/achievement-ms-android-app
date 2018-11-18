package in.stevemann.sams;

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
import in.stevemann.sams.models.AchievementModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class AchievementDetailsActivity extends AppCompatActivity {

    AchievementModel achievementModel = null;
    CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    @BindView(R.id.achievement_details_event_name)
    TextView _eventNameText;
    @BindView(R.id.achievement_details_roll_no)
    TextView _rollNoText;
    @BindView(R.id.achievement_details_section)
    TextView _sectionText;
    @BindView(R.id.achievement_details_sessionFrom)
    TextView _sessionFromText;
    @BindView(R.id.achievement_details_sessionTo)
    TextView _sessionToText;
    @BindView(R.id.achievement_details_semester)
    TextView _semester;
    @BindView(R.id.achievement_details_participated_or_organised)
    TextView _participatedOrOrganisedText;
    @BindView(R.id.achievement_details_shift)
    TextView _shiftText;
    @BindView(R.id.achievement_details_student_name)
    TextView _studentNameText;
    @BindView(R.id.achievement_details_date)
    TextView _eventDateText;
    @BindView(R.id.achievement_details_title)
    TextView _titleAwardedText;
    @BindView(R.id.achievement_details_venue)
    TextView _eventVenueText;
    @BindView(R.id.achievement_details_category)
    TextView _categoryText;
    @BindView(R.id.achievement_details_description)
    TextView _eventDescriptionText;
    @BindView(R.id.achievement_details_rating)
    TextView _ratingText;
    @BindView(R.id.achievement_details_approved_by)
    TextView _approvedByText;
    @BindView(R.id.achievement_details_approved)
    TextView _approvedText;
    @BindView(R.id.achievement_details_imageUrl)
    TextView _imageUrlText;
    @BindView(R.id.btn_delete_achievement)
    Button _deleteAchievementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_details);
        ButterKnife.bind(this);
        achievementModel = Objects.requireNonNull(getIntent().getExtras()).getParcelable("achievementObj");

        System.out.println(TokenUtil.dataExists(this));
        if (TokenUtil.dataExists(this)) {
            _deleteAchievementButton.setEnabled(true);
            _deleteAchievementButton.setVisibility(View.VISIBLE);

            _deleteAchievementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = new ProgressDialog(v.getContext(),
                            R.style.AppTheme_Light_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Deleting Achievement...");
                    progressDialog.show();

                    String encryptedData = TokenUtil.readData(getApplicationContext());
                    String[] data = encryptedData.split(" ");
                    String encryptedToken = data[1];
                    String iv = data[0];
                    String token = cryptoUtil.decryptToken(encryptedToken, iv);

                    RequestParams params = new RequestParams(); //this bloody thing just doesn't work with RequestParams so embedding the params in the URL is the only option. Way to go!

                    new RESTClient().post("achievements/delete?id="+achievementModel.getId()+"&token="+token, params, new JsonHttpResponseHandler() {
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
                                                onDeleteSuccess();
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
                                                onDeleteFailed();
                                                progressDialog.dismiss();
                                            }
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
            });
        }
        else{
            _deleteAchievementButton.setVisibility(View.GONE);
        }

        _eventNameText.setText(achievementModel.getEventName());
        _approvedByText.setText(achievementModel.getApprovedBy());
        _titleAwardedText.setText(achievementModel.getTitle());
        _approvedText.setText(String.valueOf(achievementModel.isApproved()));
        _categoryText.setText(achievementModel.getCategory());
        _eventDateText.setText(achievementModel.getDate());
        _eventDescriptionText.setText(achievementModel.getDescription());
        _eventVenueText.setText(achievementModel.getVenue());
        _ratingText.setText(achievementModel.getRating());
        _imageUrlText.setText(achievementModel.getImageUrl());
        _studentNameText.setText(achievementModel.getName());
        _shiftText.setText(achievementModel.getShift());
        _participatedOrOrganisedText.setText(achievementModel.isParticipated()?"Yes":"No");
        _rollNoText.setText(achievementModel.getRollNo());
        _sectionText.setText(achievementModel.getSection());
        _sessionFromText.setText(achievementModel.getSessionFrom());
        _sessionToText.setText(achievementModel.getSessionTo());
        _semester.setText(String.valueOf(achievementModel.getSemester()));

    }

    public void onDeleteSuccess() {
        _deleteAchievementButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Achievement Deletion Successful", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onDeleteFailed() {
        Toast.makeText(getBaseContext(), "Achievement Deletion failed", Toast.LENGTH_LONG).show();
        _deleteAchievementButton.setEnabled(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
