package objects;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/*
 Created by manohar on 12/2/15.
 */
public class Category {
    public String main_category;
    public ArrayList<String> sub_categories = new ArrayList<>();
    public Boolean show_profile;
    
    public Category(Boolean show_profile){
        this.show_profile = show_profile;
        main_category = null;
    }

    public Category(String mc, JSONArray sc){
        main_category = mc;
        this.show_profile = false;
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
