package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
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
    public static String UrlOfNotice = "http://172.25.55.156/notices/";
    public static String UrlOfLogin = "http://172.25.55.156:8080/peoplesearch/";
    private ActionBarDrawerToggle mDrawerToggle;

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

        httpPost1 = new HttpGet(UrlOfNotice+"get_constants/");
        String constants = null;
        try {
            constants = new ConnectTaskHttpGet().execute(httpPost1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        changingFragment("all");
        getSupportActionBar().setIcon(R.drawable.ic_drawer);

        parsing = new Parsing();

        categories = parsing.parse_constants(constants);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                //R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle("NoticeBoard");
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerList.setAdapter(new CustomDrawerListAdapter(this,
                R.layout.drawerlist_itemview, categories));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        SharedPreferences.Editor editor = settings.edit();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if(id == R.id.logout){
            HttpPost httpPost = new HttpPost(
                    UrlOfLogin+"logout/");
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

   /* @Override
    public boolean onSearchRequested(){
        return super.onSearchRequested();
    }*/
}