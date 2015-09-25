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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import adapters.CustomDrawerListAdapter;
import connections.ConnectTaskHttpGet;
import connections.ConnectTaskHttpPost;
import objects.Category;
import utilities.Parsing;
import objects.User;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public static String UrlOfNotice = "https://channeli.in/notices/";//"http://172.25.55.156/notices/";
    public static String UrlOfLogin = "https://channeli.in/peoplesearch/"; //http://172.25.55.156:8080/peoplesearch/";
    private ActionBarDrawerToggle mDrawerToggle;
    public static String NoticeType;

    HttpGet httpPost1;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<Category> categories;
    String session_key;
    Parsing parsing;

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpPost1 = new HttpGet(UrlOfNotice+"get_constants/");
        String constants = null;
        AsyncTask<HttpGet, Void, String> mTask;
        try {
            //ExecutorService executor = Executors.newSingleThreadExecutor();
            mTask = new ConnectTaskHttpGet().execute(httpPost1);
            //executor.invokeAll(Arrays.asList(new Task()), 10, TimeUnit.MINUTES);
            constants = mTask.get(4000, TimeUnit.MILLISECONDS);
            mTask.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setIcon(R.drawable.ic_drawer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
        parsing = new Parsing();

        categories = new ArrayList<>();
        categories.add(new Category(true));
        categories.add(new Category());
        categories.addAll(parsing.parse_constants(constants));
        categories.add(new Category("space"));
        categories.add(new Category("Logout"));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        changingFragment("All");
        setTitle("All New");
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
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        User user = new User(settings.getString("name",""), settings.getString("info",""),
                settings.getString("enrollment_no",""));
        mDrawerList.setAdapter(new CustomDrawerListAdapter(this,
                R.layout.drawerlist_itemview, categories,user));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcolor));
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

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
        args.putString("noticetype",NoticeType);
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
        //int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(categories.get(position).main_category.contains("Logout")){
                SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = settings.edit();
                HttpPost httpPost = new HttpPost(
                        UrlOfLogin+"logout/");
                session_key = settings.getString("session_key", "");
                List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
                namevaluepair.add(new BasicNameValuePair("session_key",session_key));
                try {

                    httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                    String result = new ConnectTaskHttpPost().execute(httpPost).get();
                    Log.e("...",result);
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    if(msg.equals("OK")) {
                        //Log.e("Log_tag",s.msg);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "logged out successfully", Toast.LENGTH_SHORT);
                        toast.show();

                        editor.putString("session_key", "");
                        editor.putString("flag", "NO");
                        editor.commit();
                        finish();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Failed to logout. Try later.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            else if(!categories.get(position).main_category.equals("space") &&
                    !categories.get(position).main_category.equals("null"))
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        changingFragment(categories.get(position).main_category);
        mDrawerList.setItemChecked(position,true);
        setTitle(categories.get(position).main_category+" "
                +Character.toUpperCase(NoticeType.charAt(0))+NoticeType.substring(1));
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

    public void onBackPressed(){
        super.onBackPressed();

        System.exit(0);
        //TODO close the app
    }
}