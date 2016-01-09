package connections;

import android.provider.SyncStateContract;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/*
Created by manohar on 11/2/15.
 */
public class Connections {
    public String getData(String url){
        String CSRFTOKEN="";

        try{

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.execute(new HttpGet(url));
            CookieStore cookieStore = httpClient.getCookieStore();
            List<Cookie> cookies =  cookieStore.getCookies();
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("csrftoken")) {
                    CSRFTOKEN = cookie.getValue();
                }
            }
        }
        catch(Exception e){
            Log.e("log_tag", "csrf token" + e.toString());
        }
//convert response to string

        return CSRFTOKEN;
    }
}
