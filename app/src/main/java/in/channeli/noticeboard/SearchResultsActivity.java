package in.channeli.noticeboard;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomSearchAdapter;
import adapters.CustomSpinnerAdapter;
import connections.ConnectTaskHttpGet;
import connections.SearchService;
import objects.NoticeInfo;
import utilities.DownloadResultReceiver;
import utilities.Parsing;

/*
 Created by manohar on 9/3/15.
 */
public class SearchResultsActivity extends ActionBarActivity {
    String query;
    String searchUrl;
    Parsing parsing;
    ArrayList<NoticeInfo> noticelist;
    CustomSearchAdapter customSearchAdapter;
    ProgressDialog dialog;

    String noticetype;
    String[] type = {"New","Old"};
    DownloadResultReceiver resultReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        dialog.setCancelable(false);

        parsing = new Parsing();
        noticetype = "new";
        setTitle("Searched Results");
        searchUrl = MainActivity.UrlOfNotice+"search/"+noticetype+"/All/All/?q=";
        handleIntent(getIntent());
        String url = searchUrl+query;
        final ListView lv = (ListView) findViewById(R.id.search_list_view);
        //Log.e("url sent for searching",url);
        //HttpGet httpGet = new HttpGet(url);

        try {
            //result = new ConnectTaskHttpGet(this).execute(httpGet).get();
            resultReceiver = new DownloadResultReceiver(new Handler());
            resultReceiver.setReceiver(new DownloadResultReceiver.Receiver() {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    try{
                        String result = resultData.getString("result");
                        noticelist = parsing.parseSearchedNotices(result);
                        if(!isOnline()){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Sorry! Could not connect. Check the internet connection!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else if(noticelist.size() == 0)
                            noticelist.add(new NoticeInfo());
                        if(noticelist != null) {
                            customSearchAdapter = new CustomSearchAdapter(getApplicationContext(),
                                    R.layout.list_itemview, noticelist);

                            lv.setAdapter(customSearchAdapter);
                            lv.setOnItemClickListener(new SearchItemClickListener());
                        }
                        dialog.dismiss();
                    }
                    catch(Exception e){
                        e.printStackTrace();

                    }
                }
            });
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
                    SearchService.class);
            intent.putExtra("receiver", resultReceiver);
            intent.putExtra("url", url);
            startService(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcolor));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.toggle).getActionView();
        if(null!= spinner) {
            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this,
                    R.layout.spinner_item, type);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0)
                        noticetype = "old";
                    else
                        noticetype = "new";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (null != searchView )
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText)
            {
                return true;
            }
            public boolean onQueryTextSubmit(String newText)
            {
                query = newText;
                query = query.replaceAll(" ","%20");
                //ProgressDialog dialog = ProgressDialog.show(SearchResultsActivity.this, "Loading", "Please wait...", true);
                onTextSubmit();
                //dialog.dismiss();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY);
            query = query.replaceAll(" ","%20");
        }
    }

    private void onTextSubmit(){
        dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
        dialog.setCancelable(false);

        String url = MainActivity.UrlOfNotice+"search/"+noticetype+"/All/All/?q="+query;
        Log.e("url sent for searching",url);
        //HttpGet httpGet = new HttpGet(url);
        //String result;
        try {
            //result = new ConnectTaskHttpGet(this).execute(httpGet).get();
            resultReceiver = new DownloadResultReceiver(new Handler());
            resultReceiver.setReceiver(new DownloadResultReceiver.Receiver() {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    try{
                        String result = resultData.getString("result");
                        noticelist.clear();
                        noticelist.addAll(parsing.parseSearchedNotices(result));
                        if(!isOnline()){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Sorry! Could not connect. Check the internet connection!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else if(noticelist.size() == 0)
                            noticelist.add(new NoticeInfo());
                        customSearchAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    catch(Exception e){
                        e.printStackTrace();

                    }
                }
            });
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
                    SearchService.class);
            intent.putExtra("receiver", resultReceiver);
            intent.putExtra("url", url);
            startService(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class SearchItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!noticelist.get(position).getCategory().equals("")) {
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                intent.putExtra("noticeinfo", noticelist.get(position).getContent());
                startActivity(intent);
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
