package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
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
import android.view.ViewOutlineProvider;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomListAdapter;
import connections.ConnectTaskHttpGet;
import connections.Connections;
import objects_and_parsing.NoticeObject;
import objects_and_parsing.Parsing;

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
    final String noticeurl = "http://172.25.55.156:8000/notices/get_notice/";

    @TargetApi(21)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
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
        else {
            view = inflater.inflate(R.layout.recycler_view, container, false);
        }
        Bundle args = getArguments();
        String category = args.getString("category","All");
        httpPost = new HttpGet("http://172.25.55.156:8000/notices/content_first_time_notices1/1");
        String content_first_time_notice = null;
        try {
            content_first_time_notice = new ConnectTaskHttpGet().execute(httpPost).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        con = new Connections();
        parsing = new Parsing();
        ArrayList<NoticeObject> noticelist = parsing.parseNotices(content_first_time_notice);
        Log.e("...",noticelist.get(0).getSubject());
        Log.e("...",noticelist.get(1).getSubject());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CustomListAdapter(noticelist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new RecyclerListListener(noticelist));
        mLayoutManager = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerScrollListener());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());//mAdapter, mRecyclerView));
        return view;
    }

    private class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount;
        int visibleThreshold = 5;
        int previousTotal = 0;
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = mRecyclerView.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

            if(loading){
                if(totalItemCount > previousTotal){
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if(!loading && (totalItemCount - previousTotal)<=(visibleItemCount+visibleThreshold)){

                loading = false;
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
            Intent intent = new Intent(getActivity(), Notice.class);
            intent.putExtra("noticeinfo", result);
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
                        result = new ConnectTaskHttpGet().execute(httpPost).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    ArrayList<NoticeObject> noticelist = parsing.parseNotices(result);
                    mAdapter = new CustomListAdapter(noticelist);
                    mAdapter.SetOnItemClickListener(new RecyclerListListener(noticelist));
                    swipeRefreshLayout.setRefreshing(false);
                }
            },5000);

        }
    }
}
