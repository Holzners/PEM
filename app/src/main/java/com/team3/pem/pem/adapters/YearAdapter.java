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
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Adapter for Year View of Medical Journal
 */
public class YearAdapter extends ArrayAdapter {
    private MainActivity activity;
    private int width;
    private int selectedYear;
    private FeedReaderDBHelper mDBHelper;

    public YearAdapter(MainActivity context, int resource, int width, int selectedYear) {
        super(context, resource);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.activity = context;
        this.width = width;
        this.selectedYear = selectedYear;
    }
    @Override
    public int getCount() {
        return mDBHelper.getFactorList().size()+1;
    }

    @Override
    public String getItem(int position) {
        return (position == 0) ? "" : mDBHelper.getFactorList().get(position-1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get factors and their enalbed state
        HashMap<String,Boolean> factorsEnabled = mDBHelper.getFactorEnabledMap();
        //Inflate Layout
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(R.layout.row_year_layout, null);
        //Get Textviews
        TableRow rowContainer = (TableRow)newRow.findViewById(R.id.yearContainer);
        TextView [] textViews = new TextView[12];
        int[] ratingsMonths = new int[12];

        if(position != 0){
            if(!factorsEnabled.get(getItem(position))) return newRow;
            //if factor is enabled calculate Month Averages
            ratingsMonths = getYearAverageForMonths(selectedYear, getItem(position));
        }
        //Init layout for TextBoxes
        for(int i = 0; i < textViews.length; i ++){
            textViews[i] = new TextView(activity);
            textViews[i].setHeight(width / 12 - 2);
            textViews[i].setWidth(width / 12 - 2);
            textViews[i].setBackgroundResource(R.drawable.border);

            if(position == 0){
                GradientDrawable gd = (GradientDrawable)  textViews[i].getBackground();
                gd.setColor(activity.getResources().getColor(R.color.transparent));
                textViews[i].setText(i + 1 + "");
                textViews[i].setTextColor(activity.getResources().getColor(R.color.caldroid_gray));
                textViews[i].setGravity(Gravity.CENTER);
            }else{
                //Set Background Color by Factor and Ratings
                GradientDrawable gd = (GradientDrawable) textViews[i].getBackground();
                gd.setColor(activity.getResources().getColor(
                        RatingToColorHelper.ratingToColor(getItem(position), ratingsMonths[i])));

            }
            //Add OnClick
            textViews[i].setOnClickListener(new OnMonthClickListener(getItem(position), i+1, selectedYear));
            rowContainer.addView(textViews[i]);
        }

        // Markierung des heutigen Tages
        if((selectedYear+"").equals( DateTime.today(TimeZone.getDefault()).getYear()+"")) {
            int i = DateTime.today(TimeZone.getDefault()).getMonth();
            textViews[i - 1].setTextColor(newRow.getResources().getColor(R.color.primaryColor));
        }
        return newRow;
    }

    /**
     * get Year Average ratings (each rating == average month Rating) for selected Factor
     */
    private int[] getYearAverageForMonths(int year, String factor){
        int[] result = new int[12];
        List<String> factorSingleList = new ArrayList<>();
        factorSingleList.add(factor);

        for(int i = 0 ; i < result.length ; i++){
            //Get all entries for Month for Single Symptom
            HashMap<DateTime, DayEntry> entriesMonth = mDBHelper.getDatabaseEntriesMonth(factorSingleList,i+1, year);
            int countRatings = 0;
            int averageRating = 0;
            //Calulate Average for month
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

    /**
     * NEW Year is selected
     * @param year
     */
    public void setSelectedYear (int year){
        this.selectedYear = year;
        this.notifyDataSetChanged();
    }

    public class OnMonthClickListener implements View.OnClickListener{

        private String factor;
        private int month, year;

        /**
         * Listener for Month Cells OnClick go To Month View
         * @param factor
         * @param month
         * @param year
         */
        public OnMonthClickListener(String factor , int month, int year){
            this.factor = factor;
            this.month = month;
            this.year = year;
        }

        @Override
        public void onClick(View v) {
                activity.goToMonth(month ,year , factor);
        }
    }

}
