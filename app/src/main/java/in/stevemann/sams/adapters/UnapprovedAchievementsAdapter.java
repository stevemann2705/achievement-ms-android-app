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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.stevemann.sams.AchievementDetailsActivity;
import in.stevemann.sams.AchievementModel;
import in.stevemann.sams.DashboardActivity;
import in.stevemann.sams.R;
import in.stevemann.sams.RESTClient;
import in.stevemann.sams.SignupActivity;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.TokenUtil;

public class UnapprovedAchievementsAdapter extends RecyclerView.Adapter<UnapprovedAchievementsAdapter.ViewHolder> {

    private List<AchievementModel> achievementModels;
    private Context context;

    CryptoUtil cryptoUtil = CryptoUtil.getInstance();
    RESTClient client = new RESTClient();

    public UnapprovedAchievementsAdapter(List<AchievementModel> achievementModels, Context context) {
        this.achievementModels = achievementModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_unapproved_achievement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final AchievementModel achievementModel = achievementModels.get(position);

        holder.textViewEventName.setText(achievementModel.getEventName());
        holder.textViewRollNo.setText(achievementModel.getRollNo());

        holder.buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(context,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Approving Achievement...");
                progressDialog.show();

                String id = achievementModel.getId();
                String encryptedData = TokenUtil.readData(context);
                String[] data = encryptedData.split(" ");
                String encryptedToken = data[1];
                String iv = data[0];

                final String token = cryptoUtil.decryptToken(encryptedToken, iv);

                RequestParams params = new RequestParams();
                
                RESTClient.post("achievements/approve?id="+id+"&token="+token, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                        boolean response = false;
                        try {
                            response = (boolean) timeline.get("bool");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response){
                            holder.buttonApprove.setEnabled(false);
                            Intent intent = new Intent(context, DashboardActivity.class);
                            context.startActivity(intent);
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(context, "Unable to approve achievement", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable){
                        progressDialog.dismiss();
                        System.out.println(error);
                        Log.e("ACHIEVEMENTS DATA LOAD", String.valueOf(statusCode));
                    }
                });
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AchievementDetailsActivity.class).putExtra("achievementObj",achievementModel);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println(achievementModels.size());
        return achievementModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewEventName;
        public TextView textViewRollNo;
        public AppCompatButton buttonApprove;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textViewEventNameUnapproved);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNoUnapproved);
            buttonApprove = itemView.findViewById(R.id.button_approve_achievement);
            linearLayout = itemView.findViewById(R.id.linear_layout_unapproved_achievement_card_external);
        }
    }
}
