package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import connections.AsyncDrawable;
import connections.BitmapWorkerTask;
import connections.TaskCanceler;
import in.channeli.noticeboard.MainActivity;
import in.channeli.noticeboard.R;
import objects.Category;
import objects.User;
import utilities.RoundImageView;

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

    public View getView(int position, View ConvertView, ViewGroup parent){
        View drawerlist_view = null;
        //int flag=0;
        try {
            if (categories.get(position).show_profile == false &&
                    categories.get(position).isSpinner == false) {
                //Log.e("main category", categories.get(position).main_category);
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(layout, null, true);

                ImageView imageview = (ImageView) drawerlist_view.findViewById(R.id.drawer_icons);
                if(categories.get(position).main_category.contains("Authorities")) {
                    imageview.setImageResource(R.drawable.ic_account_balance_black_24dp);
                }
                else if(categories.get(position).main_category.contains("All")) {
                    imageview.setImageResource(R.drawable.ic_home_black_24dp);
                }
                else if(categories.get(position).main_category.contains("Placement")) {
                    imageview.setImageResource(R.drawable.ic_assignment_ind_black_24dp);
                }
                else if(categories.get(position).main_category.contains("Department")) {
                    imageview.setImageResource(R.drawable.ic_school_black_24dp);
                }
                else if(categories.get(position).main_category.contains("Logout")) {
                    imageview.setImageResource(R.drawable.ic_power_settings_new_black_24dp);
                }
                TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
                textView.setText((categories.get(position)).main_category);
            }
            else if(categories.get(position).show_profile == true){
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.navigation_profile, null, true);
                TextView name = (TextView) drawerlist_view.findViewById(R.id.name);
                name.setText(user.getName());
                TextView info = (TextView) drawerlist_view.findViewById(R.id.info);
                info.setText(user.getInfo());
                RoundImageView imageView = (RoundImageView) drawerlist_view.findViewById(R.id.profile_picture);
                //imageView.setImageResource(R.drawable.profile_photo);
                String imageurl = "http://people.iitr.ernet.in/photo/";
                StringBuilder stringBuilder = new StringBuilder(imageurl+user.getEnrollmentno()+"/");
                imageurl = stringBuilder.toString();

                try{
                    Handler handler = new Handler(Looper.getMainLooper());
                    Bitmap profile_photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_photo);
                    BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView);
                    TaskCanceler taskCanceler = new TaskCanceler(bitmapWorkerTask);
                    handler.postDelayed(taskCanceler, 4*1000);
                    AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), profile_photo, bitmapWorkerTask);
                    imageView.setImageDrawable(asyncDrawable);
                    bitmapWorkerTask.execute(imageurl);
                    if(taskCanceler != null && handler != null) {
                        handler.removeCallbacks(taskCanceler);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.spinner_view, null, true);
                final String[] type = {"Current Notices","Expired Notices"};
                Spinner s = (Spinner) drawerlist_view.findViewById(R.id.drawer_spinner);
                CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context,
                        R.layout.spinner_item, type);

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

        return drawerlist_view;
    }
}
