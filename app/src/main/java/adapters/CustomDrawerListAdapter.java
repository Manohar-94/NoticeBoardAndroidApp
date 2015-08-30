package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import connections.ProfilePicDisplay;
import in.channeli.noticeboard.MainActivity;
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
        int flag=0;
        try {
            if (categories.get(position).show_profile == false &&
                    categories.get(position).isSpinner == false) {
                Log.e("main category", categories.get(position).main_category);
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(layout, null, true);

                ImageView imageview = (ImageView) drawerlist_view.findViewById(R.id.drawer_icons);
                if(categories.get(position).main_category.contains("Authorities")) {
                    imageview.setImageResource(R.drawable.ic_account_balance_black_24dp);
                    flag=1;
                }
                else if(categories.get(position).main_category.contains("All")) {
                    imageview.setImageResource(R.drawable.ic_home_black_24dp);
                    flag=1;
                }
                else if(categories.get(position).main_category.contains("Placement")) {
                    imageview.setImageResource(R.drawable.ic_assignment_ind_black_24dp);
                    flag=1;
                }
                else if(categories.get(position).main_category.contains("Department")) {
                    imageview.setImageResource(R.drawable.ic_school_black_24dp);
                    flag=1;
                }
                else if(categories.get(position).main_category.contains("logout"))
                    imageview.setImageResource(R.drawable.ic_power_settings_new_black_24dp);
                TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
                if(flag != 0) {
                    textView.setText(categories.get(position).main_category + " notices");
                }
                else
                    textView.setText((categories.get(position)).main_category);
            }
            else if(categories.get(position).show_profile == true){
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
            else{
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.spinner_view, null, true);
                final String[] type = {"current notices","expired notices"};
                Spinner s = (Spinner) drawerlist_view.findViewById(R.id.drawer_spinner);
                CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context,
                        R.layout.spinner_item, type);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                //        android.R.layout.simple_spinner_item, type);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0)
                            MainActivity.NoticeType = "old";
                        else
                            MainActivity.NoticeType = "new";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
            catch(Exception e){
                e.printStackTrace();
            }
        //Log.e("inside drawer adapter", categories.get(position).main_category);
        return drawerlist_view;
    }
}
