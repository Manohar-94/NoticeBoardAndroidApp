package connections;

/*
Created by manohar on 3/2/15.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConnectTaskHttpPost extends AsyncTask<HttpPost, Void, String> {
    @Override
    protected String doInBackground(HttpPost... httpPosts) {
// TODO Auto-generated method stub
        InputStream isr = null;
        String result="";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPosts[0]);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e){
            e.printStackTrace();
        }
//convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }
            isr.close();
            result = sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return result;
    }
}