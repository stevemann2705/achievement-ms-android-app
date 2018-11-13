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

import in.stevemann.sams.AcademicDetailsActivity;
import in.stevemann.sams.DashboardActivity;
import in.stevemann.sams.R;
import in.stevemann.sams.models.AcademicModel;

public class AcademicsAdapter extends RecyclerView.Adapter<AcademicsAdapter.ViewHolder> {
    private List<AcademicModel> academicModels;
    private Context context;

    public AcademicsAdapter(List<AcademicModel> academicModels, Context context) {
        this.academicModels = academicModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AcademicsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_academics, parent, false);
        return new AcademicsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicsAdapter.ViewHolder holder, int position) {
        final AcademicModel academicModel = academicModels.get(position);

        holder.textViewName.setText(academicModel.getName());
        holder.textViewRollNo.setText(academicModel.getRollNo());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AcademicDetailsActivity.class).putExtra("achievementObj", academicModel);
                intent.putExtra("classFrom", DashboardActivity.class.toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return academicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewRollNo;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewEventNameApproved);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNoApproved);
            linearLayout = itemView.findViewById(R.id.linear_layout_approved_achievement_card);
        }
    }
}
