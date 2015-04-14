package objects_and_parsing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 Created by manohar on 12/2/15.
 */
public class Parsing {
    JSONObject jsonObject, jsonObject2;
    JSONArray jsonArray;
    ArrayList<Categories> categorieslist;
    ArrayList<NoticeObject> noticeslist;
    ArrayList<NoticeInfo> noticeInfoList;
    Categories categories;
    NoticeObject notice;
    NoticeInfo noticeInfo;

    public ArrayList<Categories> parse_constants(String constants){
        categorieslist = new ArrayList<>();
        try {
            Log.e("value of constants",constants);
            jsonObject = new JSONObject(constants);
            jsonArray = jsonObject.getJSONArray("order");
            for(int i=0;i<jsonArray.length();i++){
                categories = new Categories(jsonArray.getString(i),
                        jsonObject.getJSONArray(jsonArray.getString(i)));
                categorieslist.add(categories);
                Log.e("parsing",jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categorieslist;
    }

    public ArrayList<NoticeObject> parseNotices(String notices){
        noticeslist = new ArrayList<>();
        try{
            jsonArray = new JSONArray(notices);

            for(int i=0;i< jsonArray.length();i++){
                jsonObject2 = jsonArray.getJSONObject(i);
                notice = new NoticeObject();
                notice.id = jsonObject2.getInt("id");
                notice.subject = jsonObject2.getString("subject");
                notice.datetime_modified = jsonObject2.getString("datetime_modified");
                notice.username = jsonObject2.getString("username");
                notice.category = jsonObject2.getString("category");
                notice.main_category = jsonObject2.getString("main_category");
                noticeslist.add(notice);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return noticeslist;
    }

    public ArrayList<NoticeInfo> parseSearchedNotices(String result){
        noticeInfoList = new ArrayList<>();
        try {

            jsonArray = new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                noticeInfo = parseNoticeInfo(jsonObject.toString());/*new NoticeInfo();
                noticeInfo.id = jsonObject.getInt("id");
                noticeInfo.reference = jsonObject.getString("reference");
                noticeInfo.subject = jsonObject.getString("subject");
                noticeInfo.username = jsonObject.getString("username");
                noticeInfo.category = jsonObject.getString("category");
                noticeInfo.content = jsonObject.getString("content");
                noticeInfo.datetime_modified = jsonObject.getString("datetime_modified");*/
                noticeInfoList.add(noticeInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeInfoList;
    }

    public NoticeInfo parseNoticeInfo(String noticeinfo){
        noticeInfo = new NoticeInfo();
        try{
            jsonObject2 = new JSONObject(noticeinfo);
            noticeInfo.id = jsonObject2.getInt("id");
            noticeInfo.content = jsonObject2.getString("content");
            noticeInfo.subject = jsonObject2.getString("subject");
            noticeInfo.datetime_modified = jsonObject2.getString("datetime_modified");
            noticeInfo.username = jsonObject2.getString("username");
            noticeInfo.category = jsonObject2.getString("category");
            }
        catch(JSONException e){
            e.printStackTrace();
        }
        return noticeInfo;
    }
}
