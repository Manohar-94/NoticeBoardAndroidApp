package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import connections.ProfilePicDisplay;
import in.channeli.noticeboard.R;
import objects.Category;
import objects.User;

/*
Created by manohar on 12/2/15.
 */
public class CustomDrawerListAdapter extends ArrayAdapter<Category> {

    private final Context context;
    private final ArrayList<Category> categories;
    private final int layout;
    private User user;

    public CustomDrawerListAdapter(Context context, int layout, ArrayList<Category> categories, User user){
        super(context, layout, categories);
        this.context = context;
        this.categories = categories;
        this.layout = layout;
        this.user = user;
    }

    public int getCount(){return categories.size();}

    //public void setData(ArrayList<Categories> categories){this.categories = categories;}

    public View getView(int position, View ConvertView, ViewGroup parent){
        View drawerlist_view = null;

        try {
            if (categories.get(position).show_profile == false) {
                Log.e("main category", categories.get(position).main_category);
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(layout, null, true);

                TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
                textView.setText(categories.get(position).main_category);

            }
            else{
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.navigation_profile, null, true);
                ImageView imageView = (ImageView) drawerlist_view.findViewById(R.id.profile_picture);
                String imageurl = "http://people.iitr.ernet.in/photo/";
                StringBuilder stringBuilder = new StringBuilder(imageurl+user.getEnrollmentno()+"/");
                imageurl = stringBuilder.toString();
                try{
                    Bitmap bitmap = new ProfilePicDisplay().execute(imageurl).get();
                    imageView.setImageBitmap(bitmap);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                TextView name = (TextView) drawerlist_view.findViewById(R.id.name);
                name.setText(user.getName());
                TextView info = (TextView) drawerlist_view.findViewById(R.id.info);
                info.setText(user.getInfo());
            }
        }
            catch(Exception e){
                e.printStackTrace();
            }
        //Log.e("inside drawer adapter", categories.get(position).main_category);
        return drawerlist_view;
    }
}
