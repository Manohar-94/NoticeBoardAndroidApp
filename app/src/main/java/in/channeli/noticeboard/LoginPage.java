package in.channeli.noticeboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import connections.ConnectTaskHttpPost;

/*
 Created by manohar on 4/2/15.
 */
public class LoginPage extends Activity{
    public static final String PREFS_NAME = "MyPrefsFile";
    public String result;
    public int check;
    public String username="", password="", session_key="", msg="", name, info;
    public View view;

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
                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.e("log_tag", "error in processData");
                }
            }
        });

    }

    public void processData() throws UnsupportedEncodingException {

        username = usertext.getText().toString();
        password = passtext.getText().toString();
        //httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(MainActivity.UrlOfLogin+"channeli_login/");
        List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(2);
        namevaluepair.add(new BasicNameValuePair("username",username));
        namevaluepair.add(new BasicNameValuePair("password",password));
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
            result = new ConnectTaskHttpPost().execute(httpPost).get();
            parseData();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

        if(msg.equals("YES")){
            editor.putString("name", name);
            editor.putString("info", info);
            editor.putString("enrollment_no", username);
            editor.putString("session_key",session_key);
            editor.putString("flag", msg);
            editor.commit();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Sorry! Could not login. Try again later!", Toast.LENGTH_LONG);
            toast.show();
            finish();
            //TODO close the app
        }
    }
    public void parseData(){
        try {
            Log.e("jsonobject",result);
            JSONObject json = new JSONObject(result);
            msg = json.getString("msg");
            name = json.getString("_name");
            info = json.getString("info");
            session_key = json.getString("session_variable");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        System.exit(0);

        //TODO close the app
    }
}
