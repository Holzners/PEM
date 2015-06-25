package com.team3.pem.pem.view.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 22.06.15.
 */
public class WeekViewAdapter extends ArrayAdapter<String> {

    FeedReaderDBHelper mDBHelper;
    Context context;
    List<String> factors;
    private int displayWidth;

    public WeekViewAdapter(Context context, int resource, List<String> factors, int displaySize) {
        super(context, resource);
        this.context = context;
        this.factors = factors;
        this.displayWidth = displaySize;
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newRow = inflater.inflate(R.layout.table_row_week, null);
        TextView[] rowViews = new TextView[7];
        rowViews[0] = (TextView) newRow.findViewById(R.id.rowTextView1);
        rowViews[1] = (TextView) newRow.findViewById(R.id.rowTextView2);
        rowViews[2] = (TextView) newRow.findViewById(R.id.rowTextView3);
        rowViews[3] = (TextView) newRow.findViewById(R.id.rowTextView4);
        rowViews[4] = (TextView) newRow.findViewById(R.id.rowTextView5);
        rowViews[5] = (TextView) newRow.findViewById(R.id.rowTextView6);
        rowViews[6] = (TextView) newRow.findViewById(R.id.rowTextView7);

        if(position == 0){
            rowViews[0].setText("SO");
            rowViews[1].setText("MO");
            rowViews[2].setText("DI");
            rowViews[3].setText("MI");
            rowViews[4].setText("DO");
            rowViews[5].setText("FR");
            rowViews[6].setText("SA");
            for(int k = 0 ; k <rowViews.length; k++){
                GradientDrawable gd = (GradientDrawable) rowViews[k].getBackground();
                gd.setColor(newRow.getResources().getColor(R.color.caldroid_lighter_gray));
                ViewGroup.LayoutParams params = rowViews[k].getLayoutParams();
                params.height = displayWidth/7-5;
                params.width = displayWidth/7-5;
                rowViews[k].setLayoutParams(params);
            }

        }else {
            List<String> factors = new ArrayList<>();
            factors.add(this.factors.get(position));
            DateTime startDay = firstDayOfThisWeek();
            DateTime endDay = lastDayOfThisWeek();

            HashMap<Date, DayEntry> entryHashMap;
            if (mDBHelper == null) {
                mDBHelper = mDBHelper.getInstance();
            }
            entryHashMap = mDBHelper.getDatabaseEntriesWeek(factors, startDay.getDay(),
                    startDay.getMonth(), startDay.getYear(), endDay.getDay(), endDay.getMonth(), endDay.getYear());


            for (int i = 0; i < rowViews.length; i++) {

                DateTime thisDate = startDay.plusDays(i);
                Date date = new Date(thisDate.getYear(), thisDate.getMonth(), thisDate.getDay());
                if (entryHashMap.containsKey(date)&& (entryHashMap.get(date).ratings.get(0) != 0)) {

                        Log.d("" + date + " Symptom:" , ""+factors.get(0) + " Farbe: " + entryHashMap.get(date).ratings.get(0));
                        GradientDrawable gd = (GradientDrawable) rowViews[i].getBackground();
                        gd.setColor(newRow.getResources().getColor(
                                RatingToColorHelper.ratingToColor(factors.get(0),
                                entryHashMap.get(date).ratings.get(0))));
                    }else{
                        GradientDrawable gd = (GradientDrawable) rowViews[i].getBackground();
                        gd.setColor(newRow.getResources().getColor(R.color.white));
                    }
                ViewGroup.LayoutParams params = rowViews[i].getLayoutParams();
                params.height = displayWidth/7-5;
                params.width = displayWidth/7-5;
                rowViews[i].setLayoutParams(params);
            }
        }

        return newRow;

    }

    private DateTime firstDayOfThisWeek() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime firstDayThisWeek = today; //start value
        int todaysWeekday = today.getWeekDay();
        int SUNDAY = 1;
        if (todaysWeekday > SUNDAY) {
            int numDaysFromSunday = todaysWeekday - SUNDAY;
            firstDayThisWeek = today.minusDays(numDaysFromSunday);
        }
        return firstDayThisWeek;
    }

    private DateTime lastDayOfThisWeek() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime lastDayOfThisWeek = today; //start value
        int todaysWeekday = today.getWeekDay();
        int SATURDAY = 7;
        if (todaysWeekday < SATURDAY) {
            int numDaysFromSunday = SATURDAY - todaysWeekday;
            lastDayOfThisWeek = today.plusDays(numDaysFromSunday);
        }
        return lastDayOfThisWeek;
    }

    public List<String> getFactors() {
        return factors;
    }

    public void setFactors(List<String> factors) {
        this.factors = factors;
    }

}
