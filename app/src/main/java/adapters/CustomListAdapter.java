package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.channeli.noticeboard.R;

/*
Created by manohar on 2/2/15.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder>{

    private int[] mDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }

    }

    public CustomListAdapter(int[] mDataset){
        this.mDataset = mDataset;
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void onBindViewHolder(ViewHolder holder, int position){
        //holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount(){
        return mDataset.length;
    }
}
