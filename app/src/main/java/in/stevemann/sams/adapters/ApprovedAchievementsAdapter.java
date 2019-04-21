package in.stevemann.sams.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.stevemann.sams.R;
import in.stevemann.sams.activities.AchievementDetailsActivity;
import in.stevemann.sams.activities.DashboardActivity;
import in.stevemann.sams.models.AchievementModel;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class ApprovedAchievementsAdapter extends RecyclerView.Adapter<ApprovedAchievementsAdapter.ViewHolder> {

    private final List<AchievementModel> achievementModels;
    private final Context context;
    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    public ApprovedAchievementsAdapter(List<AchievementModel> achievementModels, Context context) {
        this.achievementModels = achievementModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_approved_achievement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AchievementModel achievementModel = achievementModels.get(position);

        holder.textViewEventName.setText(achievementModel.getEventName());
        holder.textViewRollNo.setText(achievementModel.getRollNo());

        if (TokenUtil.dataExists(context)) {
            holder.buttonUnapprove.setEnabled(true);
            holder.buttonUnapprove.setVisibility(View.VISIBLE);

            holder.buttonUnapprove.setOnClickListener(v -> {
                final ProgressDialog progressDialog = new ProgressDialog(context,
                        R.style.AppTheme_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Unapproving Achievement...");
                progressDialog.show();

                String id = achievementModel.getId();
                String encryptedData = TokenUtil.readData(context);
                String[] data = encryptedData.split(" ");
                String encryptedToken = data[1];
                String iv = data[0];

                final String token = cryptoUtil.decryptToken(encryptedToken, iv);

                RequestParams params = new RequestParams();

                RESTClient.post("achievements/unapprove?id=" + id + "&token=" + token, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                        boolean response = false;
                        try {
                            response = (boolean) timeline.get("bool");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response) {
                            Intent intent = new Intent(context, DashboardActivity.class);
                            context.startActivity(intent);
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Unable to unapprove achievement", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                        progressDialog.dismiss();
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                        Log.d("Caused By : ", "" + throwable.getCause());
                    }
                });
            });
        } else {
            holder.buttonUnapprove.setVisibility(View.GONE);
        }

        holder.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AchievementDetailsActivity.class).putExtra("achievementObj",achievementModel);
            intent.putExtra("classFrom", DashboardActivity.class.toString());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return achievementModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewEventName;
        final TextView textViewRollNo;
        final AppCompatButton buttonUnapprove;
        final LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textViewEventNameApproved);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNoApproved);
            buttonUnapprove = itemView.findViewById(R.id.button_unapprove_achievement);
            linearLayout = itemView.findViewById(R.id.linear_layout_approved_achievement_card);
        }
    }
}
