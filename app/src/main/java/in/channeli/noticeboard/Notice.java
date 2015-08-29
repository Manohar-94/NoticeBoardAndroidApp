package in.channeli.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;


import java.util.ArrayList;

/*
Created by manohar on 19/2/15.
 */
public class Notice extends ActionBarActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        Intent intent = getIntent();
        String result = intent.getStringExtra("noticeinfo");
        /*TextView textView = (TextView) findViewById(R.id.TextViewForNotice);
        textView.setText(Html.fromHtml(result));
        Document doc = Jsoup.parse(result);
        Elements pdf_url = doc.select("a[href$=.pdf]");*/
        if(result.contains("img")  || result.contains("href")) {
            ArrayList<Integer> count = new ArrayList<>();
            StringBuffer stringBuffer = new StringBuffer(result);
            String add = "https://channeli.in";
            for(int index = result.indexOf("/media");
                    index >= 0;
                    index = result.indexOf("/media",index + 1)) {
                count.add(index);

                /*stringBuffer = stringBuffer.insert(separatedInd, add);
                result = stringBuffer.toString();*/
            }
            int prev = 0;
            for(int i=0; i< count.size(); i++){
                stringBuffer = stringBuffer.insert(prev + count.get(i), add);
                prev = (i+1)*add.length();
            }
            result = stringBuffer.toString();
        }
        Log.e("notice",result);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(result, "text/html", "utf-8");
    }
}
