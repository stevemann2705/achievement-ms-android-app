package in.stevemann.sams;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.adapters.ApprovedAchievementsAdapter;

public class ApprovedAchievementsLayout extends AppCompatActivity {

    RESTClient client = new RESTClient();

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_achievements_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(ApprovedAchievementsLayout.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        RequestParams params = new RequestParams();

        client.get("achievements/all", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                //System.out.println(timeline.length());
                for(int i = 0; i<timeline.length(); i++){
                    JSONObject o = null;
                    ListItem item = null;
                    try {
                        o = timeline.getJSONObject(i);
                        item = new ListItem(o.getString("eventName"), o.getString("rollNo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listItems.add(item);
                }

                //System.out.println(listItems.size());
                progressDialog.dismiss();
                adapter = new ApprovedAchievementsAdapter(listItems, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                progressDialog.dismiss();
                System.out.println("Error");
                Log.e("ACHIEVEMENTS DATA LOAD", String.valueOf(statusCode));
                // TODO: Handle onFailure() for signup
            }
        });
    }
}
