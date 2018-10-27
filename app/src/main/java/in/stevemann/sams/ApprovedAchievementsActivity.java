package in.stevemann.sams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.adapters.ApprovedAchievementsAdapter;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.TokenUtil;

public class ApprovedAchievementsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AchievementModel> achievementModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_achievements_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        achievementModels = new ArrayList<>();

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(ApprovedAchievementsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        RequestParams params = new RequestParams();

        RESTClient.get("achievements/all", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                //System.out.println(timeline.length());
                for(int i = 0; i<timeline.length(); i++){
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
                adapter = new ApprovedAchievementsAdapter(achievementModels, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                progressDialog.dismiss();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_password) {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_reset_profile) {
            Intent intent = new Intent(this, ResetProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_login) {
            TokenUtil.deleteData(this);
            CryptoUtil.deleteEncryption();
            Log.i("LOGOUT: ", "Deleted existing token and logged out successfully.");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
