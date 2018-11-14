package in.stevemann.sams;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.stevemann.sams.models.AcademicModel;

public class AcademicDetailsActivity extends AppCompatActivity {

    AcademicModel academicModel = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_details);
        ButterKnife.bind(this);
        academicModel = Objects.requireNonNull(getIntent().getExtras()).getParcelable("academicObj");

        _batch.setText(academicModel.getBatch());
        _studentName.setText(academicModel.getName());
        _category.setText(academicModel.getCategory());
        _programme.setText(academicModel.getProgramme());
        _rollNo.setText(academicModel.getRollNo());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
