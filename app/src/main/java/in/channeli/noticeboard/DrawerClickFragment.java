package in.channeli.noticeboard;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    RecyclerView mRecyclerView;
    CustomListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.recycler_view, container, false);
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
        final Connections con = new Connections();
        Parsing parsing = new Parsing();
        final String notice_info = "http://172.25.55.156:8000/notices/get_notice/";
        final ArrayList<NoticeObject> noticelist = parsing.parseNotices(content_first_time_notice);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CustomListAdapter(noticelist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(notice_info+noticelist.get(position).getId());
                String url = stringBuilder.toString();
                String result = con.getData(url);
                Intent intent = new Intent(getActivity(), Notice.class);
                intent.putExtra("noticeinfo", result);
                startActivity(intent);
                //NoticeInfo noticeInfo = parsing.parseNoticeInfo(result);
            }
        });
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setOnScrollListener(new RecyclerScrollListener());
        return view;
    }
    private class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){

        }
    }
}
