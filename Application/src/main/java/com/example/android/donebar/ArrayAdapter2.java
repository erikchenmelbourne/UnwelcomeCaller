package com.example.android.donebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrayAdapter2 extends ArrayAdapter<JSONObject> {

    private final Context context;
    private final ArrayList<JSONObject> jsonobjarraylist;

    public ArrayAdapter2(Context context, ArrayList<JSONObject> arraylist) {
        super(context, R.layout.row_listitem, arraylist);
        this.context = context;
        this.jsonobjarraylist = arraylist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String numberstring = "";
        String informationstring = "";
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.row_listitem, parent, false);

        TextView numbertextview = (TextView) rowview.findViewById(R.id.listitemnumber);

        try {
            numberstring = jsonobjarraylist.get(position).getString("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        numbertextview.setText(numberstring);

        TextView informationtextview = (TextView) rowview.findViewById(R.id.listiteminformation);

        try {
            informationstring = jsonobjarraylist.get(position).getString("information");
        } catch (JSONException e) {
            e.printStackTrace();
        }

       informationtextview.setText(informationstring);

        return rowview;
    }

}
