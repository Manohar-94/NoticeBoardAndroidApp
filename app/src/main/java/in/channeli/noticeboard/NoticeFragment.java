package in.channeli.noticeboard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
Created by manohar on 2/2/15.
 */
public class NoticeFragment extends Fragment {

    public static String NOTICE_NUMBER;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.recyclerlist_itemview, container, false);
    }
}
