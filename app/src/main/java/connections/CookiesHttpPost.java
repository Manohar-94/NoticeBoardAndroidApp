package connections;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;

/*
Created by manohar on 26/11/15.
 */
public class CookiesHttpPost extends AsyncTask<HttpPost, Void, String[]>{
    String[] cookie_list;

    @Override
    protected String[] doInBackground(HttpPost[] params) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        cookie_list = new String[2];
        try {
            httpClient.execute(params[0]);
            CookieStore cookieStore = httpClient.getCookieStore();
            List<Cookie> cookies =  cookieStore.getCookies();
            for (Cookie cookie: cookies) {
                if (cookie.getDomain().equals(".iitr.ernet.in") && cookie.getName().equals("CHANNELI_SESSID")){
                    cookie_list[1] = cookie.getValue();

                }
                if (cookie.getDomain().equals(".iitr.ernet.in") && cookie.getName().equals("csrftoken")) {
                    cookie_list[0] = cookie.getValue();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cookie_list;
    }
}
