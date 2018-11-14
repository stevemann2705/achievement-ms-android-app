package in.stevemann.sams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.tabs.AcademicsTab;
import in.stevemann.sams.tabs.ApprovedTab;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.RESTClient;
import in.stevemann.sams.utils.TokenUtil;

public class HomeActivity extends AppCompatActivity {

    CryptoUtil cryptoUtil = CryptoUtil.getInstance();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private HomeActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = findViewById(R.id.floatbutton_add_achievement);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAchievementActivity.class);
                startActivity(intent);
            }
        });

        if (tokenExists()) {
            String encryptedData = TokenUtil.readData(this);
            String[] data = encryptedData.split(" ");
            String encryptedToken = data[1];
            String iv = data[0];

            String token = cryptoUtil.decryptToken(encryptedToken, iv);

            RequestParams params = new RequestParams();
            params.put("token", token);

            RESTClient.get("users/isvalid", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                    if (statusCode != 401) {
                        Log.i("TOKEN: ", "Yep, token exists, and is valid.");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i("TOKEN: ", "Yep, token exists, but is not valid.");
                        Toast.makeText(getBaseContext(), "Token Expired. Please login again.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Failed: ", "" + statusCode);
                    Log.d("Error : ", "" + throwable);
                    Log.d("Caused By : ", "" + throwable.getCause());
                }
            });

        }

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container_home);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs_home);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                setFabVisibility(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setFabVisibility(final int position) {
        FloatingActionButton fab = findViewById(R.id.floatbutton_add_achievement);
        if (position == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_achievement) {
            Intent intent = new Intent(this, AddAchievementActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_login) {
            TokenUtil.deleteData(this);
            CryptoUtil.deleteEncryption();
            Log.i("LOGOUT: ", "Deleted existing token and logged out successfully.");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchAchievementActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ApprovedTab approvedTab = new ApprovedTab();
                    return approvedTab;
                case 1:
                    AcademicsTab academicsTab = new AcademicsTab();
                    return academicsTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    private boolean tokenExists() {
        return TokenUtil.dataExists(this);
    }
}
