package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 29.06.15.
 */
public class YearAdapter extends ArrayAdapter {
    private MainActivity activity;
    private int width;
    private HashMap<String, String> factorColorMap;
    private List<String> factors;
    int selectedYear;
    private FeedReaderDBHelper mDBHelper;

    public YearAdapter(MainActivity context, int resource, int width, int selectedYear, HashMap<String, String> factorColorMap) {
        super(context, resource);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.activity = context;
        this.width = width;
        this.selectedYear = selectedYear;
        this.factorColorMap = factorColorMap;
        this.factors = new ArrayList<>();
        this.factors.add("");
        for (Map.Entry<String , String>  e : this.factorColorMap.entrySet()){
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

        HashMap<String,Boolean> factorsEnabled = activity.getFactorsEnabledMap();


        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(R.layout.year_row_layout, null);
        TableRow rowContainer = (TableRow)newRow.findViewById(R.id.yearContainer);
        TextView [] textViews = new TextView[12];
        int[] ratingsMonths = new int[12];
        if(position != 0){
            if(!factorsEnabled.get(getItem(position))) return newRow;

            ratingsMonths = getYearAverageForMonths(selectedYear, getItem(position));
        }
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
            }else{
                GradientDrawable gd = (GradientDrawable) textViews[i].getBackground();
                gd.setColor(activity.getResources().getColor(
                        RatingToColorHelper.ratingToColor(getItem(position), ratingsMonths[i])));

            }
            rowContainer.addView(textViews[i]);
        }

        return newRow;
    }

    private int[] getYearAverageForMonths(int year, String factor){
        int[] result = new int[12];
        List<String> factorSingleList = new ArrayList<>();
        factorSingleList.add(factor);
        for(int i = 0 ; i < result.length ; i++){
            HashMap<DateTime, DayEntry> entriesMonth = mDBHelper.getDatabaseEntriesMonth(factorSingleList,i+1, year);
            int countRatings = 0;
            int averageRating = 0;
            for(Map.Entry<DateTime, DayEntry> e : entriesMonth.entrySet() ){
                if(e.getValue().ratings== null) continue;
                else if(e.getValue().ratings.get(factor)== 0) continue;
                averageRating += e.getValue().ratings.get(factor);
                countRatings ++;
            }
            if (countRatings!=0)averageRating = Math.round(averageRating / countRatings);
            result[i] = averageRating;
        }
        return result;
    }

    public void setFactorColorMap(HashMap<String, String> factorColorMap){
        this.factorColorMap = factorColorMap;
        this.factors.retainAll(factors);
        this.factors.add("");
        for (Map.Entry<String , String>  e : this.factorColorMap.entrySet()){
            factors.add(e.getKey());
        }
    }

    public void setSelectedYear (int year){
        this.selectedYear = year;
        this.notifyDataSetChanged();
    }

}