package connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import in.channeli.noticeboard.SearchResultsActivity;

/*
 * Created by manohar on 21/2/15.
 */
public class ConnectTaskHttpGet extends AsyncTask<HttpGet, Void, String> {
    Context context;
    ProgressDialog progressDialog;
    int check;
    public ConnectTaskHttpGet(){
        check=0;
    }
    public ConnectTaskHttpGet(Context context){
        this.context = context;
        check=1;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        if(check ==1) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }
    @Override
    protected String doInBackground(HttpGet... httpPosts) {
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
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if(check == 1) {
            Log.e("Log_tag", "inside onPostExecute");
            progressDialog.dismiss();
        }
    }
}
