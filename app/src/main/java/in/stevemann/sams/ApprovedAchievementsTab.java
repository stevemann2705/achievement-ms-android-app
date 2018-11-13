package in.stevemann.sams;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.adapters.ApprovedAchievementsAdapter;
import in.stevemann.sams.models.AchievementModel;
import in.stevemann.sams.utils.RESTClient;

public class ApprovedAchievementsTab extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AchievementModel> achievementModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_approved_achievements, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewApproved);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        achievementModels = new ArrayList<>();

        loadRecyclerViewData();
        return rootView;
    }

    private void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Approved Data...");
        progressDialog.show();

        RequestParams params = new RequestParams();

        RESTClient.get("achievements/all", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
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
                adapter = new ApprovedAchievementsAdapter(achievementModels, getContext());
                recyclerView.setAdapter(adapter);
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
