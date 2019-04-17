package in.stevemann.sams.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.stevemann.sams.R;
import in.stevemann.sams.models.TeacherAchievementModel;
import in.stevemann.sams.utils.CryptoUtil;

public class MyTeacherAchievementsAdapter extends RecyclerView.Adapter<MyTeacherAchievementsAdapter.ViewHolder> {
    private final List<TeacherAchievementModel> achievementModels;
    private final Context context;

    private final CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    public MyTeacherAchievementsAdapter(List<TeacherAchievementModel> achievementModels, Context context) {
        this.achievementModels = achievementModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyTeacherAchievementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_teacher_achievement, parent, false);
        return new MyTeacherAchievementsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyTeacherAchievementsAdapter.ViewHolder holder, int position) {
        final TeacherAchievementModel achievementModel = achievementModels.get(position);

        holder.textViewAchievement.setText(achievementModel.getTopic());
        holder.textViewDescription.setText((achievementModel.getDescription().length() < 30) ? achievementModel.getDescription() + "..." : achievementModel.getDescription().substring(0, 30) + "...");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: Add TeacherAchievementDetailsActivity and start activity on click
//                Intent intent = new Intent(v.getContext(), AchievementDetailsActivity.class).putExtra("achievementObj",achievementModel);
//                intent.putExtra("classFrom", DashboardActivity.class.toString());
//                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievementModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewAchievement;
        final TextView textViewDescription;
        final LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            textViewAchievement = itemView.findViewById(R.id.textViewTeacherAchievement);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            linearLayout = itemView.findViewById(R.id.linear_layout_teacher_achievement_card);
        }
    }
}
