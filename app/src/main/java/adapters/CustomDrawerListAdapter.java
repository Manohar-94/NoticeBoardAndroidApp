package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import connections.ProfilePicService;
import in.channeli.noticeboard.MainActivity;
import in.channeli.noticeboard.R;
import objects.Category;
import objects.User;
import utilities.DownloadResultReceiver;
import utilities.RoundImageView;

/*
Created by manohar on 12/2/15.
 */
public class CustomDrawerListAdapter extends ArrayAdapter<Category> {

    private final Context context;
    private final ArrayList<Category> categories;
    private final int layout;
    private User user;
    DownloadResultReceiver resultReceiver;

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
            if (!categories.get(position).show_profile &&
                    !categories.get(position).isSpinner &&
                    !categories.get(position).main_category.equals("space")) {
                //Log.e("main category", categories.get(position).main_category);
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(layout, null, true);
                TextView textView = (TextView) drawerlist_view.findViewById(R.id.drawer_list_text);
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
                else if(categories.get(position).main_category.contains("Feedback")){
                    imageview.setImageResource(R.drawable.ic_info_black_24dp);
                }

                textView.setText((categories.get(position)).main_category);
            }
            else if(categories.get(position).main_category.equals("space")){
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                drawerlist_view = inflater.inflate(R.layout.line_space,null, true);

            }
            else if(categories.get(position).show_profile == true){
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.navigation_profile, null, true);
                TextView name = (TextView) drawerlist_view.findViewById(R.id.name);
                name.setText(user.getName());
                TextView info = (TextView) drawerlist_view.findViewById(R.id.info);
                info.setText(user.getInfo());
                final RoundImageView imageView = (RoundImageView) drawerlist_view.findViewById(R.id.profile_picture);
                String imageurl = "http://people.iitr.ernet.in/photo/";
                StringBuilder stringBuilder = new StringBuilder(imageurl+user.getEnrollmentno()+"/");
                imageurl = stringBuilder.toString();
                imageView.setImageResource(R.drawable.profile_photo);
                try{

                    resultReceiver = new DownloadResultReceiver(new Handler());
                    resultReceiver.setReceiver(new DownloadResultReceiver.Receiver() {
                        @Override
                        public void onReceiveResult(int resultCode, Bundle resultData) {
                            try{
                                Bitmap bitmap = resultData.getParcelable("imagebitmap");

                                imageView.setImageBitmap(bitmap);

                            }
                            catch(Exception e){
                                e.printStackTrace();

                            }
                        }
                    });
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, context,
                            ProfilePicService.class);
                    intent.putExtra("receiver", resultReceiver);
                    intent.putExtra("imageurl", imageurl);
                    context.startService(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) drawerlist_view = inflater.inflate(R.layout.spinner_view, null, true);
                CustomSpinnerAdapter adapter;
                if(MainActivity.NoticeType.equals("new")) {
                    final String[] type = {"Current Notices", "Expired Notices"};
                    adapter = new CustomSpinnerAdapter(context,
                            R.layout.spinner_item, type);
                }
                else{
                    final String[] type = {"Expired Notices", "Current Notices"};
                    adapter = new CustomSpinnerAdapter(context,
                            R.layout.spinner_item, type);
                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner s = (Spinner) drawerlist_view.findViewById(R.id.drawer_spinner);
                s.setAdapter(adapter);
                s.getBackground().setColorFilter(context.getResources().getColor(R.color.spinnerarrow), PorterDuff.Mode.SRC_ATOP);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0 && MainActivity.NoticeType.equals("new"))
                            MainActivity.NoticeType = "old";
                        else if(position != 0)
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
