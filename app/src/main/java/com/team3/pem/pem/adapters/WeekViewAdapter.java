package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 22.06.15.
 */
public class WeekViewAdapter extends ArrayAdapter<String> {

    FeedReaderDBHelper mDBHelper;
    MainActivity context;

    private int displayWidth;

    private DateTime firstDayOfSelectedWeek;

    public WeekViewAdapter(MainActivity context, int resource,int displaySize) {
        super(context, resource);
        this.context = context;
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.displayWidth = displaySize;
        this.firstDayOfSelectedWeek = firstDayOfThisWeek();
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newRow = inflater.inflate(R.layout.row_week_table, null);
        TextView[] rowViews = new TextView[7];
        rowViews[0] = (TextView) newRow.findViewById(R.id.rowTextView1);
        rowViews[1] = (TextView) newRow.findViewById(R.id.rowTextView2);
        rowViews[2] = (TextView) newRow.findViewById(R.id.rowTextView3);
        rowViews[3] = (TextView) newRow.findViewById(R.id.rowTextView4);
        rowViews[4] = (TextView) newRow.findViewById(R.id.rowTextView5);
        rowViews[5] = (TextView) newRow.findViewById(R.id.rowTextView6);
        rowViews[6] = (TextView) newRow.findViewById(R.id.rowTextView7);

        if(position == 0){
            for(int k = 0 ; k <rowViews.length; k++){
                rowViews[k].setText(context.getResources().getStringArray(R.array.days)[k]);
                rowViews[k].setTextColor(newRow.getResources().getColor(R.color.caldroid_gray));
                GradientDrawable gd = (GradientDrawable) rowViews[k].getBackground();
                gd.setColor(newRow.getResources().getColor(R.color.transparent));
                ViewGroup.LayoutParams params = rowViews[k].getLayoutParams();
                params.height = displayWidth/7-5;
                params.width = displayWidth/7-5;
                rowViews[k].setLayoutParams(params);
                final int finalK = k;
                if(k > 0)
                    rowViews[k].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.showDetailDay(firstDayOfSelectedWeek.plusDays(finalK));
                        }
                    });
            }

        }else {
            if (mDBHelper.getFactorEnabledMap().get(this.getItem(position))) {
                List<String> factors = new ArrayList<>();
                factors.add(this.getItem(position));
                DateTime startDay = getFirstDayOfSelectedWeek();
                DateTime endDay = lastDayOfThisWeek();

                HashMap<DateTime, DayEntry> entryHashMap;
                if (mDBHelper == null) {
                    mDBHelper = mDBHelper.getInstance();
                }
                entryHashMap = mDBHelper.getDatabaseEntriesWeek(factors, startDay.getDay(),
                        startDay.getMonth(), startDay.getYear(), endDay.getDay(), endDay.getMonth(), endDay.getYear());


                for (int i = 0; i < rowViews.length; i++) {
                    DateTime thisDate = startDay.plusDays(i);
                    if (entryHashMap.containsKey(thisDate)) {
                        GradientDrawable gd = (GradientDrawable) rowViews[i].getBackground();
                        gd.setColor(newRow.getResources().getColor(
                                RatingToColorHelper.ratingToColor(factors.get(0),
                                        entryHashMap.get(thisDate).ratings.get(factors.get(0)))));
                    } else {
                        GradientDrawable gd = (GradientDrawable) rowViews[i].getBackground();
                        gd.setColor(newRow.getResources().getColor(R.color.white));
                    }
                    ViewGroup.LayoutParams params = rowViews[i].getLayoutParams();
                    params.height = displayWidth / 7 - 5;
                    params.width = displayWidth / 7 - 5;
                    rowViews[i].setLayoutParams(params);
                    rowViews[i].setOnClickListener(new WeekViewClickListener(context, startDay.plusDays(i)));
                }
            }else {
                for(TextView t: rowViews){ t.setVisibility(View.GONE);}
            }
        }
        // Markierung des heutigen Tages
        int i = DateTime.today(TimeZone.getDefault()).getWeekDay(); //1..7 für So..Sa
        if (i==1) i+=6;
        else i-=1;
        Log.i("WEEKDAY", String.valueOf(i));
        rowViews[i-1].setTextColor(newRow.getResources().getColor(R.color.primaryColor));

        return newRow;
    }

    private DateTime firstDayOfThisWeek() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime firstDayThisWeek = today; //start value
        int todaysWeekday = today.getWeekDay();
        if (todaysWeekday<2) todaysWeekday = 8;
        int MONDAY = 2;
        if (todaysWeekday > MONDAY) {
            int numDaysFromSunday = todaysWeekday - MONDAY;
            firstDayThisWeek = today.minusDays(numDaysFromSunday);
        }
        return firstDayThisWeek;
    }


    private DateTime lastDayOfThisWeek() {
      return getFirstDayOfSelectedWeek().plusDays(7);
    }

   public DateTime getFirstDayOfSelectedWeek() {
        return firstDayOfSelectedWeek;
    }

    public void setFirstDayOfSelectedWeek(DateTime firstDayOfSelectedWeek) {
        this.firstDayOfSelectedWeek = firstDayOfSelectedWeek;
    }

    public class WeekViewClickListener implements View.OnClickListener{

        private MainActivity context;
        private DateTime date;

        public WeekViewClickListener(MainActivity context, DateTime date) {
            this.context = context;
            this.date = date;
        }

        @Override
        public void onClick(View v) {
            context.showRateDayPopup(date);
        }
    }

}
