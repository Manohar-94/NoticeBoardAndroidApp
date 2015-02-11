package connections;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
Created by manohar on 11/2/15.
 */
public class Connections {
    public String getData(String url){
        String result="";
        InputStream isr = null;
        //String url = "http://172.25.55.5:8000/peoplesearch/index/";
        //StringBuilder sbuilder = new StringBuilder();
        //sbuilder.append(url+"?name="+srch_str+"&role="+role+"&course="+course+
        //        "&year="+year+"&faculty_department="+faculty_department+"&faculty_designation="+faculty_designation+
        //        "&services_list="+services_list+"&groups_list="+groups_list+"&counter="+counter);
        //url = sbuilder.toString();
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
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
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        return result;
    }
}
