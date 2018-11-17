package in.stevemann.sams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.models.AchievementModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class SearchAchievementActivity extends AppCompatActivity {
    private static final String TAG = "SearchAchievementActivity";

    private RequestParams params = new RequestParams();
    private ArrayList<AchievementModel> achievementModels;
    private boolean searchType; // false -> approved; true -> unapproved
    private CryptoUtil cryptoUtil = CryptoUtil.getInstance();
    String callingClass;

    @BindView(R.id.input_search_rollNo)
    EditText _rollNoText;
    @BindView(R.id.input_search_section)
    Spinner _section;
    @BindView(R.id.input_search_sessionFrom)
    EditText _sessionFromText;
    @BindView(R.id.input_search_sessionTo)
    EditText _sessionToText;
    @BindView(R.id.input_search_semester)
    Spinner _semester;
    @BindView(R.id.input_search_department)
    Spinner _department;
    @BindView(R.id.input_search_shift)
    Spinner _shift;
    @BindView(R.id.input_search_eventStartDate)
    EditText _eventStartDateText;
    @BindView(R.id.input_search_eventEndDate)
    EditText _eventEndDateText;
    @BindView(R.id.input_search_category)
    Spinner _category;
    @BindView(R.id.checkboxSearch_approved)
    CheckBox _approved;
    @BindView(R.id.checkboxSearch_unapproved)
    CheckBox _unapproved;
    @BindView(R.id.btn_search)
    Button _searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_achievement);
        ButterKnife.bind(this);

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        achievementModels = new ArrayList<>();

        callingClass = Objects.requireNonNull(this.getCallingActivity()).getClassName();

        if ("in.stevemann.sams.HomeActivity".equals(callingClass)) {
            _approved.setChecked(true);
            _approved.setEnabled(false);
            _unapproved.setChecked(false);
            _unapproved.setEnabled(false);
        } else if ("in.stevemann.sams.DashboardActivity".equals(callingClass)) {
            _approved.setEnabled(true);
            _unapproved.setEnabled(true);
        }

        //Department
        ArrayAdapter<String> adapter_department = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter_department.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_department.add("computerscience");
        adapter_department.add("education");
        adapter_department.add("management");
        adapter_department.add("Department"); //Spinner selection text

        _department.setAdapter(adapter_department);
        _department.setSelection(adapter_department.getCount()); //set the hint the default selection so it appears on launch.

        //Section
        ArrayAdapter<String> adapter_section = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter_section.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_section.add("A");
        adapter_section.add("B");
        adapter_section.add("C");
        adapter_section.add("D");
        adapter_section.add("Select Section"); //Spinner selection text

        _section.setAdapter(adapter_section);
        _section.setSelection(adapter_section.getCount()); //set the hint the default selection so it appears on launch.

        //Semester
        ArrayAdapter<String> adapter_semester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter_semester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_semester.add("1");
        adapter_semester.add("2");
        adapter_semester.add("3");
        adapter_semester.add("4");
        adapter_semester.add("5");
        adapter_semester.add("6");
        adapter_semester.add("Select Semester"); //Spinner selection text

        _semester.setAdapter(adapter_semester);
        _semester.setSelection(adapter_semester.getCount()); //set the hint the default selection so it appears on launch.

        //Shift
        ArrayAdapter<String> adapter_shift = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter_shift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_shift.add("morning");
        adapter_shift.add("evening");
        adapter_shift.add("Select Shift"); //Spinner selection text

        _shift.setAdapter(adapter_shift);
        _shift.setSelection(adapter_shift.getCount()); //set the hint the default selection so it appears on launch.

        //Category
        ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_category.add("sports");
        adapter_category.add("technical");
        adapter_category.add("cultural");
        adapter_category.add("others");
        adapter_category.add("Select Category"); //Spinner selection text

        _category.setAdapter(adapter_category);
        _category.setSelection(adapter_category.getCount()); //set the hint the default selection so it appears on launch.
    }

    private void search() {
        Log.d("SEARCHACTIVITY", "Search");

        if (!seachable()) {
            onSearchFailed();
            return;
        }

        _searchButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SearchAchievementActivity.this,
                R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        if (searchType) { // searching unapproved
            String encryptedData = TokenUtil.readData(getApplicationContext());
            String[] data = encryptedData.split(" ");
            String encryptedToken = data[1];
            String iv = data[0];

            String token = cryptoUtil.decryptToken(encryptedToken, iv);

            params.put("token", token);
            RESTClient.get("achievements/unapproved", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    achievementModels.clear();
                    //System.out.println(timeline.length());
                    for (int i = 0; i < timeline.length(); i++) {
                        JSONObject o = null;
                        AchievementModel item = null;
                        try {
                            o = timeline.getJSONObject(i);
                            item = new AchievementModel(
                                    o.getString("_id"),
                                    o.getString("eventName"),
                                    o.getString("rollNo"),
                                    o.getInt("semester"),
                                    o.getString("sessionFrom"),
                                    o.getString("name"),
                                    o.getBoolean("participated"),
                                    o.getString("description"),
                                    o.getString("shift"),
                                    o.getString("sessionTo"),
                                    o.getString("section"),
                                    o.getString("department"),
                                    o.getString("date"),
                                    o.getString("rating"),
                                    o.getString("approvedBy"),
                                    o.getString("category"),
                                    o.getString("title"),
                                    o.getString("imageUrl"),
                                    o.getBoolean("approved"),
                                    o.getString("venue")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        achievementModels.add(item);
                    }

                    progressDialog.dismiss();
                    onSearchSuccess();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    progressDialog.dismiss();
                    Log.d("Failed: ", "" + statusCode);
                    Log.d("Error : ", "" + throwable);
                    Log.d("Caused By : ", "" + throwable.getCause());
                }
            });
        } else {           // searching approved
            RESTClient.get("achievements/all", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    achievementModels.clear();
                    //System.out.println(timeline.length());
                    for (int i = 0; i < timeline.length(); i++) {
                        JSONObject o = null;
                        AchievementModel item = null;
                        try {
                            o = timeline.getJSONObject(i);
                            item = new AchievementModel(
                                    o.getString("_id"),
                                    o.getString("eventName"),
                                    o.getString("rollNo"),
                                    o.getInt("semester"),
                                    o.getString("sessionFrom"),
                                    o.getString("name"),
                                    o.getBoolean("participated"),
                                    o.getString("description"),
                                    o.getString("shift"),
                                    o.getString("sessionTo"),
                                    o.getString("section"),
                                    o.getString("department"),
                                    o.getString("date"),
                                    o.getString("rating"),
                                    o.getString("approvedBy"),
                                    o.getString("category"),
                                    o.getString("title"),
                                    o.getString("imageUrl"),
                                    o.getBoolean("approved"),
                                    o.getString("venue")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        achievementModels.add(item);
                    }

                    progressDialog.dismiss();
                    onSearchSuccess();
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onSearchSuccess() {
        _searchButton.setEnabled(true);
        Intent intent = new Intent(this, SearchResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("achievementModels", achievementModels);
        intent.putExtras(bundle);
        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSearchFailed() {
        Toast.makeText(getBaseContext(), "Achievement Search Failed", Toast.LENGTH_LONG).show();
        _searchButton.setEnabled(true);
    }

    // TODO: Validation for Search
    public boolean seachable() {
        boolean searchable = false;
        int i = 0;

        String rollNo = _rollNoText.getText().toString();
        String section = _section.getSelectedItem().toString();
        String sessionFrom = _sessionFromText.getText().toString();
        String sessionTo = _sessionToText.getText().toString();
        String semester = _semester.getSelectedItem().toString();
        String department = _department.getSelectedItem().toString();
        String shift = _shift.getSelectedItem().toString();
        String eventStartDate = _eventStartDateText.getText().toString();
        String eventEndDate = _eventEndDateText.getText().toString();
        String category = _category.getSelectedItem().toString();

        if (rollNo.length() > 0) {
            if (rollNo.length() < 3) {
                _rollNoText.setError("at least 3 characters");
                searchable = false;
            } else {
                searchable = true;
                i++;
                params.put("rollNo", rollNo);
                _rollNoText.setError(null);
            }
        }

        if ("Select Section".equals(section)) {
            //searchable = false;
        } else {
            System.out.println(section);
            searchable = true;
            i++;
            params.put("section", section);
        }

        if ("Select Semester".equals(semester)) {
            //searchable = false;
        } else {
            searchable = true;
            i++;
            params.put("semester", semester);
        }

        if ("Select Shift".equals(shift)) {
            //searchable = false;
        } else {
            searchable = true;
            i++;
            params.put("shift", shift);
        }

        if ("Select Category".equals(category)) {
            //searchable = false;
        } else {
            searchable = true;
            i++;
            params.put("category", category);
        }

        if ("Department".equals(department)) {
            //searchable = false;
        } else {
            searchable = true;
            i++;
            params.put("department", department);
        }

        if (sessionFrom.length() > 0) {
            if (sessionFrom.length() != 4) {
                _sessionFromText.setError("Session year (FORMAT: YYYY)");
                searchable = false;
            } else {
                searchable = true;
                i++;
                params.put("sessionFrom", sessionFrom);
                _sessionFromText.setError(null);
            }
        }

        if (sessionTo.length() > 0) {
            if (sessionTo.length() != 4) {
                _sessionToText.setError("Session year (FORMAT: YYYY)");
                searchable = false;
            } else {
                searchable = true;
                i++;
                params.put("sessionTo", sessionTo);
                _sessionToText.setError(null);
            }
        }

        // TODO: Event Date Validation
        if (eventEndDate.length() > 0 && eventStartDate.length() > 0) {
            if (eventStartDate.length() != 10 || eventEndDate.length() != 10) {
                _eventStartDateText.setError("Date Format: YYYY/MM/DD");
                _eventEndDateText.setError("Date Format: YYYY/MM/DD");
                searchable = false;
            } else {
                searchable = true;
                i++;
                params.put("dateFrom", eventStartDate);
                params.put("dateTo", eventEndDate);
                _eventStartDateText.setError(null);
                _eventEndDateText.setError(null);
            }
        }

        if ((!_approved.isChecked() && !_unapproved.isChecked()) || (_approved.isChecked() && _unapproved.isChecked())) {
            searchable = false;
            _unapproved.setError("Please check one checkbox");
            _approved.setError("Please check one checkbox");
        }

        if (i > 0) {
            return searchable;
        } else {
            return false;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkboxSearch_approved:
                if (checked)
                    searchType = false;
                _unapproved.setChecked(false);
                break;
            case R.id.checkboxSearch_unapproved:
                if (checked)
                    searchType = true;
                _approved.setChecked(false);
                break;
        }
    }
}
