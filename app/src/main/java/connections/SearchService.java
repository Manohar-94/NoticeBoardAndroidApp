package connections;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.SearchView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import in.channeli.noticeboard.MainActivity;

/**
 * Created by manohar on 25/9/15.
 */
public class SearchService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SearchService(String name) {
        super(name);
    }
    public SearchService(){
        super("SearchThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpGet httpGet = new HttpGet(intent.getStringExtra("url"));
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME,0);

        httpGet.setHeader("Cookie","csrftoken="+settings.getString("csrftoken",""));
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setHeader("Cookie","CHANNELI_SESSID="+settings.getString("CHANNELI_SESSID",""));
        InputStream isr = null;
        String result="";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpGet);
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
        final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        resultReceiver.send(1, bundle);
        this.stopSelf();
    }
}
