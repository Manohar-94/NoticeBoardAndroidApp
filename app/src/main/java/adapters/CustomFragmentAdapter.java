package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.channeli.noticeboard.R;
import objects.NoticeInfo;
import objects.NoticeObject;

/*
 Created by manohar on 30/8/15.
 */
public class CustomFragmentAdapter extends ArrayAdapter<NoticeObject> {
    private Context context;
    private ArrayList<NoticeObject> noticeArrayList;
    private int layout;

    public CustomFragmentAdapter(Context context, int layout, ArrayList<NoticeObject> noticeArrayList){
        super(context, layout, noticeArrayList);
        this.context = context;
        this.noticeArrayList = noticeArrayList;
        this.layout = layout;
    }

    public int getCount(){
        return noticeArrayList.size();
    }

    public void setData(ArrayList<NoticeObject> noticeArrayList){
        this.noticeArrayList = noticeArrayList;
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View searchlist_view = inflater.inflate(layout, null);
        TextView category = (TextView) searchlist_view.findViewById(R.id.recycler_list_category);
        category.setText(noticeArrayList.get(position).getCategory());
        TextView subject = (TextView) searchlist_view.findViewById(R.id.recycler_list_subject);
        subject.setText(noticeArrayList.get(position).getSubject());
        TextView datetime = (TextView) searchlist_view.findViewById(R.id.recycler_list_datetime);
        String[] date_time = noticeArrayList.get(position).getDatetime_modified().split("T");
        String date = date_time[0];
        String time = date_time[1];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String currentdate = simpleDateFormat.format(new Date());
            Date current = simpleDateFormat.parse(currentdate);
            Date strDate = simpleDateFormat.parse(date);

            if(strDate.equals(current)) {
                datetime.setText(time);
            }
            else {
                date = new SimpleDateFormat("dd-MMM-yyyy").format(strDate);
                datetime.setText(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Log.e("inside search adapter", categories.get(position).main_category);
        return searchlist_view;
    }
}