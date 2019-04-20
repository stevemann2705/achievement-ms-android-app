package in.stevemann.sams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import in.stevemann.sams.R;
import in.stevemann.sams.tabs.MyTeacherAchievementsTab;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.TokenUtil;

public class TeacherAchievementActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private TeacherAchievementActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_achievement);

        FloatingActionButton fab = findViewById(R.id.floatbutton_add_teacher_achievement);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddTeacherAchievementActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar_teacher_achievement);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TeacherAchievementActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container_teacher_achievement1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dashboard_reset_password) {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dashboard_reset_profile) {
            Intent intent = new Intent(this, ResetProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dashboard_logout) {
            TokenUtil.deleteData(this);
            CryptoUtil.deleteEncryption();
            Log.i("LOGOUT: ", "Deleted existing token and logged out successfully.");
            Toast.makeText(getBaseContext(), "Logged out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        } else if (id == R.id.action_dashboard_student) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MyTeacherAchievementsTab();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 1;
        }
    }
}
