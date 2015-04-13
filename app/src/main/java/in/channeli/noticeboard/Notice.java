package in.channeli.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import objects_and_parsing.NoticeInfo;
import objects_and_parsing.Parsing;

/*
Created by manohar on 19/2/15.
 */
public class Notice extends ActionBarActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        Intent intent = getIntent();
        String result = intent.getStringExtra("noticeinfo");
        TextView textView = (TextView) findViewById(R.id.TextViewForTemp);
        textView.setText(Html.fromHtml(result));
    }
}
