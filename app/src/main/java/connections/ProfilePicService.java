package connections;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
  Created by manohar on 24/9/15.
 */
public class ProfilePicService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ProfilePicService(String name) {
        super(name);
    }
    public ProfilePicService(){
        super("ProfilePicThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("imageurl");
        if(url != null) {
            final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
            Bundle bundle = new Bundle();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            bundle.putParcelable("imagebitmap", bitmap);
            resultReceiver.send(1, bundle);
            this.stopSelf();
        }
    }
}
