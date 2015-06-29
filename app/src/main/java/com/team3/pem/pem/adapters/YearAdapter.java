package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephan on 29.06.15.
 */
public class YearAdapter extends ArrayAdapter {
    private MainActivity activity;
    private int width;
    private HashMap<String, String> factorColorMap;
    private List<String> factors;

    public YearAdapter(MainActivity context, int resource, int width, HashMap<String, String> factorColorMap) {
        super(context, resource);
        this.activity = context;
        this.width = width;
        Log.d("Width", width+ "");
        this.factorColorMap = factorColorMap;
        this.factors = new ArrayList<>();
        factors.add("");
        for (Map.Entry<String , String>  e : factorColorMap.entrySet()){
            factors.add(e.getKey());
        }
    }
    @Override
    public int getCount() {
        return factors.size();
    }

    @Override
    public String getItem(int position) {
        return factors.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(R.layout.year_row_layout, null);
        TableRow rowContainer = (TableRow)newRow.findViewById(R.id.yearContainer);
        TextView [] textViews = new TextView[12];

        for(int i = 0; i < textViews.length; i ++){
            textViews[i] = new TextView(activity);
            textViews[i].setHeight(width / 12 - 2);
            textViews[i].setWidth(width / 12 - 2);
            textViews[i].setBackgroundResource(R.drawable.border);
            if(position == 0){
                GradientDrawable gd = (GradientDrawable)  textViews[i].getBackground();
                gd.setColor(activity.getResources().getColor(R.color.caldroid_lighter_gray));
                textViews[i].setText(i + 1 + "");
                textViews[i].setGravity(Gravity.CENTER);
            }
            rowContainer.addView(textViews[i]);
        }

        return newRow;
    }
}
