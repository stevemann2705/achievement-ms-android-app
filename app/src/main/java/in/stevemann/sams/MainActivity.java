package in.stevemann.sams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.stevemann.sams.utils.CryptoUtil;
import in.stevemann.sams.utils.TokenUtil;

public class MainActivity extends AppCompatActivity {
    CryptoUtil cryptoUtil = CryptoUtil.getInstance();
    RESTClient client = new RESTClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!tokenExists()){
            Log.i("TOKEN: ","Token doesn't even exist");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {
            String encryptedData = TokenUtil.readData(this);
            String[] data = encryptedData.split(" ");
            String encryptedToken = data[1];
            String iv = data[0];

            String token = cryptoUtil.decryptToken(encryptedToken, iv);

            RequestParams params = new RequestParams();
            params.put("token", token);


            client.get("users/isvalid", params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                    if(statusCode != 401){
                        Log.i("TOKEN: ","Yep, token exists, and is valid.");
                    } else{
                        Log.i("TOKEN: ","Yep, token exists, but is not valid.");
                        Toast.makeText(getBaseContext(), "Token Expired. Please login again.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                    // TODO: Handle onFailure() for token validity check.
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_password){
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_reset_profile){
            Intent intent = new Intent(this, ResetProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout){
            TokenUtil.deleteData(this);
            cryptoUtil.deleteEncryption();
            Log.i("LOGOUT: ","Deleted existing token and logged out successfully.");
            Toast.makeText(getBaseContext(), "Logged out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean tokenExists(){
        return TokenUtil.dataExists(this);
    }
}
