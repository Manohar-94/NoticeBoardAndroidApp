package in.channeli.noticeboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import connections.ConnectTaskHttpPost;

/*
 Created by manohar on 12/8/15.
 */

public class SplashScreen  extends Activity{
    public static final String PREFS_NAME = "MyPrefsFile";
    public String msg, session_key;

    SharedPreferences settings;
    HttpPost httpPost;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        settings = getSharedPreferences(PREFS_NAME,0);
        session_key = settings.getString("session_key","");
        msg = settings.getString("msg","NO");
        if(msg.equals("YES")){
            try{
                //httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(MainActivity.UrlOfLogin+"check_session/");
                List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
                namevaluepair.add(new BasicNameValuePair("session_key",session_key));
                httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                result = new ConnectTaskHttpPost().execute(httpPost).get();
                JSONObject json = new JSONObject(result);
                msg = json.getString("msg");
                Toast toast = Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG);
                toast.show();
                if(msg.equals("YES")){

                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
            }
            catch(Exception e){
                Log.e("log_tag", e.toString());
            }
        }
        else {
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        }
    }
}
