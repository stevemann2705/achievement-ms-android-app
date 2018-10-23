package in.stevemann.sams.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.stevemann.sams.ListItem;
import in.stevemann.sams.R;

public class ApprovedAchievementsAdapter extends RecyclerView.Adapter<ApprovedAchievementsAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public ApprovedAchievementsAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewEventName.setText(listItem.getEventName());
        holder.textViewRollNo.setText(listItem.getRollNo());
    }

    @Override
    public int getItemCount() {
        System.out.println(listItems.size());
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewEventName;
        public TextView textViewRollNo;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textViewEventName);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNo);
        }
    }
}
