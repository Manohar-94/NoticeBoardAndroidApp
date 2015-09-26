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

/*
 Created by manohar on 12/3/15.
 */
public class CustomSearchAdapter extends ArrayAdapter<NoticeInfo> {
    private Context context;
    private ArrayList<NoticeInfo> noticeInfoArrayList;
    private int layout;

    public CustomSearchAdapter(Context context, int layout, ArrayList<NoticeInfo> noticeInfoArrayList){
        super(context, layout, noticeInfoArrayList);
        this.context = context;
        this.noticeInfoArrayList = noticeInfoArrayList;
        this.layout = layout;
    }

    public int getCount(){
        return noticeInfoArrayList.size();
    }

    public void setData(ArrayList<NoticeInfo> noticeInfoArrayList){
        this.noticeInfoArrayList = noticeInfoArrayList;
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View searchlist_view = inflater.inflate(layout, null);
        TextView category = (TextView) searchlist_view.findViewById(R.id.recycler_list_category);
        category.setText(noticeInfoArrayList.get(position).getCategory());
        TextView subject = (TextView) searchlist_view.findViewById(R.id.recycler_list_subject);
        subject.setText(noticeInfoArrayList.get(position).getSubject());
        TextView datetime = (TextView) searchlist_view.findViewById(R.id.recycler_list_datetime);
        String[] date_time = noticeInfoArrayList.get(position).getDatetime_modified().split("T");

        try {
            if(date_time.length != 0) {
                String date = date_time[0];
                String time = date_time[1];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentdate = simpleDateFormat.format(new Date());
                Date current = simpleDateFormat.parse(currentdate);
                Date strDate = simpleDateFormat.parse(date);

                if (strDate.equals(current)) {
                    datetime.setText(time);
                } else {
                    date = new SimpleDateFormat("dd-MMM-yyyy").format(strDate);
                    datetime.setText(date);
                }
            }
            else
                datetime.setText("");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return searchlist_view;
    }
}
