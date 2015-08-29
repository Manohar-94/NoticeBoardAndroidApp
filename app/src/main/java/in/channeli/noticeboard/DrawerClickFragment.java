package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomListAdapter;
import connections.ConnectTaskHttpGet;
import connections.Connections;
import objects.NoticeInfo;
import objects.NoticeObject;
import utilities.Parsing;

/*
Created by manohar on 2/2/15.
 */
public class DrawerClickFragment extends Fragment {

    HttpGet httpPost;
    private RecyclerView mRecyclerView;
    private CustomListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    Connections con;
    Parsing parsing;
    final String noticeurl = MainActivity.UrlOfNotice+"get_notice/";
    String category;
    ArrayList<NoticeObject> noticelist;
    AsyncTask<HttpGet, Void, String> mTask;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @TargetApi(21)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = null;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            view = inflater.inflate(R.layout.recycler_view_lollipop, container, false);
            View refreshButton = view.findViewById(R.id.refresh_button);
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                    outline.setOval(0, 0, diameter, diameter);
                }
            };
            refreshButton.setOutlineProvider(viewOutlineProvider);
            //refreshButton.setClipToOutline(true);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else {*/
            view = inflater.inflate(R.layout.recycler_view, container, false);
        //}
        Bundle args = getArguments();
        category = args.getString("category","All");
        category = category.replaceAll(" ","%20");
        httpPost = new HttpGet(MainActivity.UrlOfNotice+"list_notices/new/"+category+"/All/1/20/0");
        String content_first_time_notice = null;

        try {
            mTask = new ConnectTaskHttpGet().execute(httpPost);
            content_first_time_notice = mTask.get();
            mTask.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        con = new Connections();
        parsing = new Parsing();
        noticelist = parsing.parseNotices(content_first_time_notice);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView = rv;
        //mRecyclerView.setHasFixedSize(true);
        mAdapter = new CustomListAdapter(noticelist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new RecyclerListListener(noticelist));
        mLayoutManager = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerScrollListener(){
            public void loadMore(){
                String result=null;

                try {
                    httpPost = new HttpGet(MainActivity.UrlOfNotice+
                            "list_notices/new/"+category+
                            "/All/1/20/"+totalItemCount);
                    mTask = new ConnectTaskHttpGet().execute(httpPost);
                    result = mTask.get();
                    mTask.cancel(true);
                } catch(Exception e){
                    e.printStackTrace();
                }
                noticelist.addAll(parsing.parseNotices(result));
                mAdapter.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());
        return view;
    }

    private abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener {

        public abstract void loadMore();

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loading = false;
                    Log.v("...", "Last Item Wow !");
                    Log.e("...",MainActivity.UrlOfNotice+
                            "list_notices/new/"+category+
                            "/All/1/20/"+noticelist.get(totalItemCount-1).getId());
                    loadMore();
                }
            }
        }

    }

    private class RecyclerListListener implements CustomListAdapter.OnItemClickListener {
        ArrayList<NoticeObject> noticelist;
        public RecyclerListListener(ArrayList<NoticeObject> noticelist){
            this.noticelist = noticelist;
        }
        @Override
        public void onItemClick(View view, int position) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(noticeurl+noticelist.get(position).getId());
            String url = stringBuilder.toString();
            httpPost = new HttpGet(url);
            String result = null;
            try {
                result = new ConnectTaskHttpGet().execute(httpPost).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            NoticeInfo noticeInfo = parsing.parseNoticeInfo(result);
            Intent intent = new Intent(getActivity(), Notice.class);
            intent.putExtra("noticeinfo", noticeInfo.getContent());
            startActivity(intent);
            //NoticeInfo noticeInfo = parsing.parseNoticeInfo(result);
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        /*CustomListAdapter mAdapter;
        RecyclerView recyclerView;*/
        /*public SwipeRefreshListener(CustomListAdapter mAdapter, RecyclerView recyclerView){
            this.mAdapter = mAdapter;
            this.recyclerView = recyclerView;
        }*/
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String result=null;
                    try {
                        httpPost = new HttpGet(MainActivity.UrlOfNotice+
                                "list_notices/new/"+category+
                                "/All/1/20/0");
                        mTask = new ConnectTaskHttpGet().execute(httpPost);
                        result = mTask.get();
                        mTask.cancel(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    noticelist.clear();
                    noticelist.addAll(parsing.parseNotices(result));
                    mAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
            },3000);

        }
    }
}
