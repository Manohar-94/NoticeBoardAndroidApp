package in.channeli.noticeboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import connections.ConnectTaskHttpGet;
import connections.ConnectTaskHttpPost;

/*
 Created by manohar on 12/8/15.
 */

public class SplashScreen  extends Activity{
    //public static final String PREFS_NAME = "MyPrefsFile";
    private static int SPLASH_TIME_OUT = 2000;
    public String msg="YES", flag, session_key, CHANNELI_SESSID;

    SharedPreferences settings;
    //HttpPost httpPost;
    HttpGet httpGet;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //if(Build.VERSION.SDK_INT >= 21){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //}
        settings = getSharedPreferences(MainActivity.PREFS_NAME,0);
        //session_key = settings.getString("session_key","");
        CHANNELI_SESSID = settings.getString("CHANNELI_SESSID","");
        //flag = settings.getString("flag","NO");
        //if(flag.equals("YES")){
        if(!CHANNELI_SESSID.equals("")){

                //httpClient = new DefaultHttpClient();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        try {
                            if(isOnline()) {
                                /*httpPost = new HttpPost(MainActivity.UrlOfLogin + "check_session/");
                                List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
                                namevaluepair.add(new BasicNameValuePair("session_key", session_key));
                                httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                                result = new ConnectTaskHttpPost().execute(httpPost).get();
                                JSONObject json = new JSONObject(result);
                                msg = json.getString("msg");
                                //Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                                //toast.show();
                                if (msg.equals("YES")) {

                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                finish();*/
                                httpGet = new HttpGet(MainActivity.UrlOfPeopleSearch+"return_details/");
                                httpGet.setHeader("Cookie","csrftoken="+settings.getString("csrftoken",""));
                                httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
                                httpGet.setHeader("Cookie","CHANNELI_SESSID="+settings.getString("CHANNELI_SESSID",""));
                                result = new ConnectTaskHttpGet().execute(httpGet).get();
                                JSONObject json = new JSONObject(result);
                                msg = json.getString("msg");
                                if (msg.equals("NO")){
                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else{
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Sorry! Could not connect. Check the internet connection!", Toast.LENGTH_SHORT);
                                toast.show();
                                finish();
                            }
                        }
                        catch(Exception e){
                            Log.e("log_tag", e.toString());
                            /*Toast toast = Toast.makeText(getApplicationContext(),"Sorry! Could not login. Try again later!", Toast.LENGTH_LONG);
                            toast.show();*/
                            Intent intent = new Intent(getApplication(), LoginPage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, SPLASH_TIME_OUT);
        }
        else {
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
        //System.exit(0);
        //TODO close the app
    }
}
