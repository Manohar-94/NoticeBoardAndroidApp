package in.channeli.noticeboard;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by manohar on 27/9/15.
 */
public class Feedback extends Activity {
    String url = "https://channeli.in/#helpcenter";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        WebView webView = (WebView) findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(url);
    }
}
