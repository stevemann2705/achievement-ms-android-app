package in.stevemann.sams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import in.stevemann.sams.adapters.AcademicsAdapter;
import in.stevemann.sams.models.AcademicModel;
import in.stevemann.sams.utils.RESTClient;

public class AcademicsTab extends Fragment {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AcademicModel> academicModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_academics, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewAcademics);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        academicModels = new ArrayList<>();

        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Academics Data...");
        progressDialog.show();

        RequestParams params = new RequestParams();


        RESTClient.get("academic/getall", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                for (int i = 0; i < timeline.length(); i++) {
                    JSONObject o = null;
                    AcademicModel item = null;
                    try {
                        o = timeline.getJSONObject(i);
                        item = new AcademicModel(
                                o.getString("_id"),
                                o.getString("rollNo"),
                                o.getString("name"),
                                o.getString("batch"),
                                o.getString("programme"),
                                o.getString("category")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    academicModels.add(item);
                }

                progressDialog.dismiss();
                adapter = new AcademicsAdapter(academicModels, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
                Log.d("Caused By : ", "" + throwable.getCause());

                if (statusCode == 0) {
                    try {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();

                            }
                        });

                        alertDialog.show();
                    } catch (Exception e) {
                        Log.d("CONNECTION", "Show Dialog: " + e.getMessage());
                    }
                }
            }
        });

    }
}
