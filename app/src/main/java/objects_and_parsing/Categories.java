package objects_and_parsing;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/*
 Created by manohar on 12/2/15.
 */
public class Categories {
    public String main_category;
    public ArrayList<String> sub_categories = new ArrayList<>();

    public Categories(String mc, JSONArray sc){
        main_category =mc;
        try {
            for(int i=0; i<sc.length(); i++){
                sub_categories.add(sc.getString(i));
                }
            }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
