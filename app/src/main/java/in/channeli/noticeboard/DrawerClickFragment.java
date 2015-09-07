package in.channeli.noticeboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import adapters.CustomFragmentAdapter;
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
    ListView listView;
    CustomFragmentAdapter customFragmentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    Connections con;
    Parsing parsing;
    final String noticeurl = MainActivity.UrlOfNotice+"get_notice/";
    String category;
    String noticetype;
    ArrayList<NoticeObject> noticelist;
    AsyncTask<HttpGet, Void, String> mTask;

    @TargetApi(21)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = null;
        view = inflater.inflate(R.layout.list_view, container, false);
        Bundle args = getArguments();
        category = args.getString("category","All");
        category = category.replaceAll(" ","%20");
        noticetype = args.getString("noticetype","new");
        httpPost = new HttpGet(MainActivity.UrlOfNotice+"list_notices/"+noticetype+"/"+category+"/All/1/20/0");
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

        final ListView lv = (ListView) view.findViewById(R.id.my_list_view);
        listView = lv;
        customFragmentAdapter = new CustomFragmentAdapter(this.getActivity(),
                R.layout.list_itemview,noticelist);
        lv.setAdapter(customFragmentAdapter);
        lv.setOnItemClickListener(new ListViewItemClickListener());
        lv.setOnScrollListener(new ListViewScrollListener(2){
            @Override
            public void loadMore(int page, int totalItemsCount){
                String result=null;
                try {
                    httpPost = new HttpGet(MainActivity.UrlOfNotice+
                            "list_notices/"+noticetype+"/"+category+
                            "/All/1/20/"+noticelist.get(totalItemsCount-1).getId());
                    mTask = new ConnectTaskHttpGet().execute(httpPost);
                    result = mTask.get();
                    mTask.cancel(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                /*Log.e("...",MainActivity.UrlOfNotice+
                        "list_notices/new/"+category+
                        "/All/1/20/"+noticelist.get(totalItemsCount-1).getId());*/
                noticelist.addAll(parsing.parseNotices(result));
                customFragmentAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.BLUE, Color.BLACK);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());
        return view;
    }

    private class ListViewItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(noticeurl+noticelist.get(position).getId());
            String url = stringBuilder.toString();
            httpPost = new HttpGet(url);
            String result = null;
            try {
                result = new ConnectTaskHttpGet(getActivity()).execute(httpPost).get();

            } catch (Exception e) {
                e.printStackTrace();

            }
            if(!result.equals("")) {
                NoticeInfo noticeInfo = parsing.parseNoticeInfo(result);
                Intent intent = new Intent(getActivity(), Notice.class);
                intent.putExtra("noticeinfo", noticeInfo.getContent());
                startActivity(intent);
            }
            else {
                Toast toast = Toast.makeText(getActivity(),
                        "Cannot connect to internet", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private abstract class ListViewScrollListener implements ListView.OnScrollListener{

        private int bufferItemCount = 2;
        private int currentPage = 0;
        private int itemCount = 0;
        private boolean isLoading = true;

        public ListViewScrollListener(int bufferItemCount){
            this.bufferItemCount = bufferItemCount;
        }

        public abstract void loadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount < itemCount) {
                this.itemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.isLoading = true; }
            }

            if (isLoading && (totalItemCount > itemCount)) {
                isLoading = false;
                itemCount = totalItemCount;
                currentPage++;
            }

            if (!isLoading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + bufferItemCount)) {
                loadMore(currentPage + 1, totalItemCount);
                isLoading = true;
            }
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String result=null;
                    try {
                        httpPost = new HttpGet(MainActivity.UrlOfNotice+
                                "list_notices/"+noticetype+"/"+category+
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
                    customFragmentAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }
            },3000);

        }
    }
}
