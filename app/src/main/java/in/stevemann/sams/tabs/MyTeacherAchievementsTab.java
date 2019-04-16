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
import in.stevemann.sams.adapters.AllTeacherAchievementsAdapter;
import in.stevemann.sams.models.TeacherAchievementModel;
import in.stevemann.sams.models.UserModel;
import in.stevemann.sams.utils.RESTClient;

public class MyTeacherAchievementsTab extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<TeacherAchievementModel> achievementModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_teacher_achievements, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewMyTeacherAchievements);
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
        progressDialog.setMessage("Loading All Teacher Achievement Data...");
        progressDialog.show();

        RequestParams params = new RequestParams();

        RESTClient.get("tachievements/allUserid?userId=" + UserModel.getUserId(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                for (int i = 0; i < timeline.length(); i++) {
                    JSONObject o = timeline;
                    TeacherAchievementModel item = null;
                    try {
                        JSONArray arr = (JSONArray) o.get("achs");
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject object = arr.getJSONObject(j);
                            item = new TeacherAchievementModel(
                                    o.getString("_id"),
                                    o.getString("taType"),
                                    o.getBoolean("international"),
                                    o.getString("topic"),
                                    o.getString("published"),
                                    o.getBoolean("sponsored"),
                                    o.getBoolean("reviewed"),
                                    o.getString("date"),
                                    o.getString("description"),
                                    o.getBoolean("msi"),
                                    o.getString("place")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    achievementModels.add(item);
                }

                progressDialog.dismiss();
                adapter = new AllTeacherAchievementsAdapter(achievementModels, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String s, Throwable throwable) {
                progressDialog.dismiss();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());
            }
        });
    }
}
