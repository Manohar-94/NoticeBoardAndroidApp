package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomDrawerListAdapter;
import adapters.CustomListAdapter;
import connections.ConnectTaskHttpGet;
import connections.Connections;
import objects_and_parsing.Categories;
import objects_and_parsing.NoticeObject;
import objects_and_parsing.Parsing;


public class MainActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private CustomListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    HttpGet httpPost1,httpPost2;

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            setContentView(R.layout.activity_main_lollipop);
            View addButton = findViewById(R.id.refresh_button);
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                    outline.setOval(0, 0, diameter, diameter);
                }
            };
            addButton.setOutlineProvider(viewOutlineProvider);
            //addButton.setClipToOutline(true);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            setContentView(R.layout.activity_main);
        }*/

        final Connections con = new Connections();

        httpPost1 = new HttpGet("http://172.25.55.156:8000/notices/get_constants/");
        httpPost2 = new HttpGet("http://172.25.55.156:8000/notices/content_first_time_notices1/1");
        String constants = null;
        String content_first_time_notice = null;
        try {
            constants = new ConnectTaskHttpGet().execute(httpPost1).get();
            content_first_time_notice = new ConnectTaskHttpGet().execute(httpPost2).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //String constants = con.getData("http://172.25.55.156:8000/notices/get_constants/");
        //String content_first_time_notice = con.getData("http://172.25.55.156:8000/notices/content_first_time_notices1/1/");
        final String notice_info = "http://172.25.55.156:8000/notices/get_notice/";

        final Parsing parsing = new Parsing();
        ArrayList<Categories> categories;
        categories = parsing.parse_constants(constants);
        final ArrayList<NoticeObject> noticelist = parsing.parseNotices(content_first_time_notice);

        //recyclerView initialization
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomListAdapter(noticelist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(notice_info+noticelist.get(position).getId());
                String url = stringBuilder.toString();
                String result = con.getData(url);
                Intent intent = new Intent(getApplicationContext(), NoticeFragment.class);
                intent.putExtra("noticeinfo", result);
                startActivity(intent);
                //NoticeInfo noticeInfo = parsing.parseNoticeInfo(result);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerScrollListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new CustomDrawerListAdapter(this,
                R.layout.drawerlist_itemview, categories));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            Fragment fragment = new NoticeFragment();
            Bundle args = new Bundle();
            args.putInt(NoticeFragment.NOTICE_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();


        }
    }

    private class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){

        }
    }
}