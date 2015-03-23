package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adapters.CustomDrawerListAdapter;
import adapters.CustomListAdapter;
import connections.ConnectTaskHttpGet;
import connections.ConnectTaskHttpPost;
import connections.Connections;
import objects_and_parsing.Categories;
import objects_and_parsing.NoticeObject;
import objects_and_parsing.Parsing;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    HttpGet httpPost1;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<Categories> categories;
    String session_key;
    Parsing parsing;

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        httpPost1 = new HttpGet("http://172.25.55.156:8000/notices/get_constants/");
        String constants = null;
        try {
            constants = new ConnectTaskHttpGet().execute(httpPost1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        changingFragment("all");

        parsing = new Parsing();

        categories = parsing.parse_constants(constants);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new CustomDrawerListAdapter(this,
                R.layout.drawerlist_itemview, categories));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    void changingFragment(String category){
        Fragment fragment = new DrawerClickFragment();
        Bundle args = new Bundle();
        args.putString("category",category);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //if(null != searchView){
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchResultsActivity.class)));
        searchView.setIconified(false);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.logout){
            HttpPost httpPost = new HttpPost(
                    "http://172.25.55.156:8000/peoplesearch/index/logout/");
            session_key = settings.getString("session_key", "");
            List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
            namevaluepair.add(new BasicNameValuePair("session_key",session_key));
            try{

                httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                String result = new ConnectTaskHttpPost().execute(httpPost).get();
                //parsing.parse_constants(result);

                //if(s.msg.equals("OK")){
                    //Log.e("Log_tag",s.msg);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "logged out successfully" , Toast.LENGTH_SHORT);
                    toast.show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("session_key","");
                    editor.commit();
                    finish();
                //}
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        changingFragment(categories.get(position).main_category);
        mDrawerList.setItemChecked(position,true);
        setTitle(categories.get(position).main_category);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    public void setTitle(String title){
        try {
            getSupportActionBar().setTitle(title);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}