package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.channeli.noticeboard.R;
import objects_and_parsing.NoticeObject;

/*
Created by manohar on 2/2/15.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder>{

    ArrayList<NoticeObject> noticelist ;
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ViewHolder(View mRecyclerView) {
            super(mRecyclerView);
            mTextView = (TextView) mRecyclerView.findViewById(R.id.recycler_list_text);
            mRecyclerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public CustomListAdapter(ArrayList<NoticeObject> noticelist){
        this.noticelist = noticelist;
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_itemview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void onBindViewHolder(ViewHolder holder, int position){

        holder.mTextView.setText(noticelist.get(position).getSubject());
    }

    @Override
    public int getItemCount(){
        return noticelist.size();
    }

}
