package in.channeli.noticeboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import connections.ProfilePicDisplay;

/*
 Created by manohar on 13/4/15.
 */
public class Profile extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String info = settings.getString("info","");
        String enrollment_no = settings.getString("enrollment_no","");
        TextView textView = (TextView) findViewById(R.id.information);
        textView.setText(info);
        ImageView imageView = (ImageView) findViewById(R.id.profile_pic);
        String imageurl = "http://people.iitr.ernet.in/photo/";
        StringBuilder stringBuilder = new StringBuilder(imageurl+enrollment_no+"/");
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

    }

}
