package in.stevemann.sams;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import in.stevemann.sams.adapters.ApprovedAchievementsAdapter;

public class SearchResultsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AchievementModel> achievementModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        achievementModels = bundle.getParcelableArrayList("achievementModels");

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        adapter = new ApprovedAchievementsAdapter(achievementModels, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
