package connections;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class ProfilePicDisplay extends AsyncTask<String, Void, Bitmap> {


    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... imageurls) {

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageurls[0]).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}