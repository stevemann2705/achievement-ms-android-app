package in.stevemann.sams.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.stevemann.sams.AchievementDetailsActivity;
import in.stevemann.sams.AchievementModel;
import in.stevemann.sams.R;

public class SeachResultsAchievementsAdapter extends RecyclerView.Adapter<SeachResultsAchievementsAdapter.ViewHolder> {
    private List<AchievementModel> achievementModels;
    private Context context;

    public SeachResultsAchievementsAdapter(List<AchievementModel> achievementModels, Context context) {
        this.achievementModels = achievementModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_results, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AchievementModel achievementModel = achievementModels.get(position);

        holder.textViewEventName.setText(achievementModel.getEventName());
        holder.textViewRollNo.setText(achievementModel.getRollNo());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AchievementDetailsActivity.class).putExtra("achievementObj", achievementModel);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievementModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewEventName;
        public TextView textViewRollNo;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textViewEventNameSearchResult);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNoSeachResult);
            linearLayout = itemView.findViewById(R.id.linear_layout_search_result_achievement_card);
        }
    }
}
