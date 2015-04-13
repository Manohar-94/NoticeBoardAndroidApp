package in.channeli.noticeboard;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomSearchAdapter;
import connections.ConnectTaskHttpGet;
import objects_and_parsing.NoticeInfo;
import objects_and_parsing.Parsing;

/*
 Created by manohar on 9/3/15.
 */
public class SearchResultsActivity extends ActionBarActivity {
    String query;
    SearchView searchView;
    String searchUrl;
    Parsing parsing;
    ArrayList<NoticeInfo> noticelist;
    CustomSearchAdapter customSearchAdapter;
    ListView listView;
    final String noticeurl = MainActivity.UrlOfNotice+"get_notice/";
    HttpGet httpPost;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        parsing = new Parsing();
        searchUrl = MainActivity.UrlOfNotice+"search/new/All/All/?q=";
        handleIntent(getIntent());
        String url = searchUrl+query;
        Log.e("url sent for searching",url);
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            result = new ConnectTaskHttpGet().execute(httpGet).get();
            noticelist = parsing.parseSearchedNotices(result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final ListView lv = (ListView) findViewById(R.id.search_list_view);
        listView = lv;
        customSearchAdapter = new CustomSearchAdapter(this,
                R.layout.recyclerlist_itemview,noticelist);
        lv.setAdapter(customSearchAdapter);
        lv.setOnItemClickListener(new SearchItemClickListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
                onTextSubmit();

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
        }
    }

    private void onTextSubmit(){
        String url = searchUrl+query;
        Log.e("url sent for searching",url);
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            result = new ConnectTaskHttpGet().execute(httpGet).get();
            noticelist = parsing.parseSearchedNotices(result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        customSearchAdapter.setData(noticelist);

        listView.setAdapter(customSearchAdapter);
    }

    public class SearchItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(getApplicationContext(), Notice.class);
            intent.putExtra("noticeinfo", noticelist.get(position).getContent());
            startActivity(intent);
        }
    }
}
