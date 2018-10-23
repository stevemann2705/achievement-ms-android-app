package in.stevemann.sams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AchievementDetailsActivity extends AppCompatActivity {

    AchievementModel achievementModel = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_details);
        ButterKnife.bind(this);
        achievementModel = Objects.requireNonNull(getIntent().getExtras()).getParcelable("achievementObj");

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
}
