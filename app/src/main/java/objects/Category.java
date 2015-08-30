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
    public Boolean isSpinner;

    public Category(){
        this.isSpinner = true;
        this.main_category = "null";
        this.show_profile = false;
    }
    public Category(String mc){
        this.show_profile = false;
        main_category = mc;
        isSpinner = false;
    }
    public Category(Boolean show_profile){
        this.show_profile = show_profile;
        main_category = "null";
        this.isSpinner = false;
    }

    public Category(String mc, JSONArray sc){
        main_category = mc;
        this.show_profile = false;
        this.isSpinner = false;
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
