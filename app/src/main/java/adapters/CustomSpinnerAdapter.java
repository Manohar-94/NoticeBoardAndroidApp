package adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.channeli.noticeboard.R;

/**
 * Created by manohar on 31/8/15.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    String[] type;
    int layout;
    Context context;

    public CustomSpinnerAdapter(Context context, int layout, String[] type){
        super(context, layout, type);
        this.context = context;
        this.layout = layout;
        this.type = type;
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        View view = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) view = inflater.inflate(layout, null, true);
        TextView textView = (TextView) view.findViewById(R.id.spinner_text);
        textView.setText(type[position]);
        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        View v = super.getDropDownView(position, convertView, parent);
        v.setBackgroundResource(android.R.color.white);
        return v;
    }
}
