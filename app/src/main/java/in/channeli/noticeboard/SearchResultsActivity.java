package in.channeli.noticeboard;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import org.apache.http.client.methods.HttpGet;

import java.util.concurrent.ExecutionException;

import connections.ConnectTaskHttpGet;
import objects_and_parsing.Parsing;

/*
 Created by manohar on 9/3/15.
 */
public class SearchResultsActivity extends Activity {
    String query;
    SearchView searchView;
    String searchUrl;
    Parsing parsing;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        handleIntent(getIntent());
        parsing = new Parsing();
        searchUrl = "172.25.55.156:8000/notices/search/new/all/all/?q=";
        searchView = (SearchView) findViewById(R.id.search_widget);
        searchView.setOnQueryTextListener(new SearchClickListener());

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

    public class SearchClickListener implements SearchView.OnQueryTextListener{

        @Override
        public boolean onQueryTextSubmit(String query) {
            String url = searchUrl+query;
            HttpGet httpGet = new HttpGet(url);
            try {
                String result = new ConnectTaskHttpGet().execute(httpGet).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}
