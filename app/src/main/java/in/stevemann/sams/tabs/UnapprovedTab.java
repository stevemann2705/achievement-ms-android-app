package in.stevemann.sams.tabs;

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

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.stevemann.sams.R;
import in.stevemann.sams.adapters.UnapprovedAchievementsAdapter;
import in.stevemann.sams.models.AchievementModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class UnapprovedTab extends Fragment {

    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AchievementModel> achievementModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_unapproved, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewUnapproved);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        achievementModels = new ArrayList<>();

        loadRecyclerViewData();
        return rootView;
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Unapproved Data...");
        progressDialog.show();

        String encryptedData = TokenUtil.readData(getContext());
        String[] data = encryptedData.split(" ");
        String encryptedToken = data[1];
        String iv = data[0];

        String token = cryptoUtil.decryptToken(encryptedToken, iv);

        RequestParams params = new RequestParams();
        params.put("token", token);

        RESTClient.get("achievements/unapproved", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                boolean response;
                JSONArray array = null;
                try {
                    response = timeline.getBoolean("bool");
                    array = timeline.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i<array.length(); i++){
                    JSONObject o = null;
                    AchievementModel item = null;
                    try {
                        o = array.getJSONObject(i);
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
                                "null",
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
                adapter = new UnapprovedAchievementsAdapter(achievementModels, getContext());
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
}
