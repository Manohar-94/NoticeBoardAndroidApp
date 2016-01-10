package in.channeli.noticeboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import connections.ConnectTaskHttpGet;
import connections.ConnectTaskHttpPost;
import connections.Connections;
import connections.CookiesHttpGet;
import connections.CookiesHttpPost;

/*
 Created by manohar on 4/2/15.
 */
public class LoginPage extends Activity{
    public String result;
    public String username="", password="", session_key="", msg="", enrollment_no="", name, info;
    public View view;
    public String[] cookie_list_new;
    HttpPost httpPost;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    LayoutInflater inflater;

    EditText usertext,passtext;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.login_page,null);

        usertext = (EditText) findViewById(R.id.username);
        passtext = (EditText) findViewById(R.id.password);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                try{
                    processData();
                    //hideKeyboard();
                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.e("log_tag", "error in processData");
                }
            }
        });

    }

    /*public void hideKeyboard(){
        if(view != null) {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/
    public void processData() throws UnsupportedEncodingException {

        username = usertext.getText().toString();
        password = passtext.getText().toString();

        try{
            HttpGet httpGet = new HttpGet(MainActivity.UrlOfLogin);
            String CSRFTOKEN = new CookiesHttpGet().execute(httpGet).get();

            Log.e("csrftoken", CSRFTOKEN);

            httpPost = new HttpPost(MainActivity.UrlOfLogin);
            List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(2);
            namevaluepair.add(new BasicNameValuePair("username",username));
            namevaluepair.add(new BasicNameValuePair("password",password));
            namevaluepair.add(new BasicNameValuePair("csrfmiddlewaretoken",CSRFTOKEN));
            namevaluepair.add(new BasicNameValuePair("remember_me","on"));

            httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
            httpPost.setHeader("Referer", "http://people.iitr.ernet.in/login/");
            httpPost.setHeader("Cookie","csrftoken="+CSRFTOKEN);
            httpPost.setHeader("Accept", "application/xml");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            BasicClientCookie csrf_cookie = new BasicClientCookie("csrftoken", CSRFTOKEN);
            csrf_cookie.setDomain(".iitr.ernet.in");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            CookieStore cookieStore = httpClient.getCookieStore();
            cookieStore.addCookie(csrf_cookie);

// Create local HTTP context - to store cookies
            HttpContext localContext = new BasicHttpContext();
// Bind custom cookie store to the local context
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            cookie_list_new = new CookiesHttpPost().execute(httpPost).get();

            HttpGet httpGet1 = new HttpGet(MainActivity.UrlOfPeopleSearch+"return_details/?username="+username);
            httpGet1.setHeader("Cookie","csrftoken="+cookie_list_new[0]);
            httpGet1.setHeader("Accept", "application/xml");
            httpGet1.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet1.setHeader("Cookie","CHANNELI_SESSID="+cookie_list_new[1]);
            result = new ConnectTaskHttpGet().execute(httpGet1).get();
            parseData();
        }
        catch(Exception e){
            e.printStackTrace();

        }
        settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        editor = settings.edit();

        if(msg.equals("YES")){
            editor.putString("name", name);
            editor.putString("info", info);
            editor.putString("enrollment_no", enrollment_no);
            editor.putString("flag", msg);
            editor.putString("csrftoken",cookie_list_new[0]);
            editor.putString("CHANNELI_SESSID",cookie_list_new[1]);
            editor.commit();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            if(!isOnline()){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Sorry! Could not connect. Check the internet connection!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(msg.contains("NO")){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Wrong username or password!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Sorry! Could not login! Try again later!", Toast.LENGTH_SHORT);
                toast.show();
            }
            finish();
            //TODO close the app
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
    public void parseData(){
        try {
            Log.e("jsonobject",result);
            JSONObject json = new JSONObject(result);
            msg = json.getString("msg");
            name = json.getString("_name");
            info = json.getString("info");
            session_key = json.getString("session_variable");
            enrollment_no = json.getString("enrollment_no");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        //System.exit(0);

        //TODO close the app
    }
}
