package objects_and_parsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 Created by manohar on 12/2/15.
 */
public class Parsing {
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Categories> categorieslist;
    Categories categories;

    public ArrayList<Categories> parse_constants(String constants){
        categorieslist = new ArrayList<>();
        try {
            jsonObject = new JSONObject(constants);
            jsonArray = jsonObject.getJSONArray("order");
            for(int i=0;i<jsonArray.length();i++){
                categories = new Categories(jsonArray.getString(i),
                        jsonObject.getJSONArray(jsonArray.getString(i)));
                categorieslist.add(categories);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categorieslist;
    }
}
