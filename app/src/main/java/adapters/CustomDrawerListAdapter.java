package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.channeli.noticeboard.R;
import objects_and_parsing.Categories;

/*
Created by manohar on 12/2/15.
 */
public class CustomDrawerListAdapter extends ArrayAdapter<Categories> {

    private Context context;
    private ArrayList<Categories> categories;

    public CustomDrawerListAdapter(Context context, int layout, ArrayList<Categories> categories){
        super(context, layout, categories);
        this.context = context;
        this.categories = categories;
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View drawerlist_view = inflater.inflate(R.layout.drawerlist_itemview, null);
        TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
        textView.setText(categories.get(position).main_category);
        //Log.e("inside drawer adapter", categories.get(position).main_category);
        return drawerlist_view;
    }
}
