package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ColorsToPick;
import com.team3.pem.pem.utili.DayEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 25.06.15.
 */
public class RateDayAdapter extends ArrayAdapter {

    HashMap<String,String> factorColors;

    List<String> factors;

    MainActivity activity;
    DateTime date;

    public RateDayAdapter(MainActivity activity, int resource, HashMap<String,String> factorColors, DateTime date) {
        super(activity, resource);
        this.activity = activity;
        this.factorColors = factorColors;
        this.date = date;
        factors = new ArrayList<>();
        activity.selectedColor = new HashMap<>();
        for (Map.Entry<String,String> e : factorColors.entrySet()){
            factors.add(e.getKey());
            activity.selectedColor.put(e.getKey(),1);
        }
    }


    public void setFactorColors(HashMap<String, String> factorColors) {
        this.factorColors = factorColors;
        factors.removeAll(factors);
        for (Map.Entry<String,String> e : factorColors.entrySet()){
            factors.add(e.getKey());
            activity.selectedColor.put(e.getKey(),1);
        }
        this.notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView[] rateViews = new TextView[5];

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newRow = inflater.inflate(R.layout.rate_day_layout, null);

        TextView factorText = (TextView) newRow.findViewById(R.id.factorText);
        factorText.setText(factors.get(position));

        rateViews[0] = (TextView) newRow.findViewById(R.id.ratText1);
        rateViews[1] = (TextView) newRow.findViewById(R.id.ratText2);
        rateViews[2] = (TextView) newRow.findViewById(R.id.ratText3);
        rateViews[3] = (TextView) newRow.findViewById(R.id.ratText4);
        rateViews[4] = (TextView) newRow.findViewById(R.id.ratText5);


        String color = factorColors.get(factors.get(position));
        int[]colors = ColorsToPick.getColorByString(color).getAllColors();

        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth()/7;
        int height = display.getWidth()/7;

        for (int i = 0 ; i < colors.length && i < rateViews.length ; i++){
            ViewGroup.LayoutParams params =rateViews[i].getLayoutParams();
            params.width = width;
            params.height = height;
            rateViews[i].setLayoutParams(params);

            GradientDrawable gd = (GradientDrawable) rateViews[i].getBackground();
            gd.setColor(newRow.getResources().getColor(colors[i]));
            gd.setStroke(1, activity.getResources().getColor(R.color.black));
            rateViews[i].setOnClickListener(
                    new RateViewOnClickListener(rateViews[i],rateViews,position,i));
        }

        FeedReaderDBHelper dbHelper = FeedReaderDBHelper.getInstance();
        DayEntry entry = dbHelper.getDatabaseEntriesDay(factors, date.getDay(), date.getMonth(), date.getYear());
        int rating = 1;
        if(entry != null) {
            HashMap<String, Integer> ratings = entry.ratings;
            rating = ratings.get(factors.get(position));
        }

        GradientDrawable gd = (GradientDrawable) rateViews[(rating == 0) ? 0 : (rating-1)].getBackground();
        gd.setStroke(7, activity.getResources().getColor(R.color.black));
        activity.selectedColor.put(factors.get(position), rating);
        return newRow;
    }

   public class RateViewOnClickListener implements View.OnClickListener {

       private TextView textView;
       private TextView[] textViews;
       private int factorPosition, index;

       public RateViewOnClickListener(TextView textView, TextView[] textViews, int factorPosition, int index){
           this.textView = textView;
           this.textViews = textViews;
           this.factorPosition = factorPosition;
           this.index = index;
       }

       @Override
       public void onClick(View v) {
           int oldIndex =  activity.selectedColor.get(factors.get(factorPosition))-1;
           oldIndex = (oldIndex == -1) ? 0 : oldIndex;
           GradientDrawable gd = (GradientDrawable)  textViews[oldIndex].getBackground();
           gd.setStroke(1, activity.getResources().getColor(R.color.black));

           activity.selectedColor.put(factors.get(factorPosition), index+1);
           GradientDrawable gdNew = (GradientDrawable)  textView.getBackground();
           gdNew.setStroke(7, activity.getResources().getColor(R.color.black));

       }
   }
}
