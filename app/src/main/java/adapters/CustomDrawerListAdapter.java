package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import in.channeli.noticeboard.R;
import objects_and_parsing.Categories;

/*
Created by manohar on 12/2/15.
 */
public class CustomDrawerListAdapter extends ArrayAdapter<Categories> {

    private final Context context;
    private final ArrayList<Categories> categories;
    private final int layout;

    public CustomDrawerListAdapter(Context context, int layout, ArrayList<Categories> categories){
        super(context, layout, categories);
        this.context = context;
        this.categories = categories;
        this.layout = layout;
    }

    public int getCount(){return categories.size();}

    //public void setData(ArrayList<Categories> categories){this.categories = categories;}

    public View getView(int position, View ConvertView, ViewGroup parent){
        View drawerlist_view = null;
        try {
            Log.e("main category",categories.get(position).main_category);
            LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater != null) drawerlist_view = inflater.inflate(layout, null, true);

            TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
            textView.setText(categories.get(position).main_category);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //Log.e("inside drawer adapter", categories.get(position).main_category);
        return drawerlist_view;
    }
}
