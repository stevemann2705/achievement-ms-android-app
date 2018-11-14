package in.stevemann.sams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.TokenUtil;

public class HomeActivity extends AppCompatActivity {

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
}
