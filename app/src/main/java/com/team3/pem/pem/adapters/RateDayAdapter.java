package com.team3.pem.pem.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ColorsToPick;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 25.06.15.
 */
public class RateDayAdapter extends ArrayAdapter {

    private MainActivity activity;
    private DateTime date;
    private FeedReaderDBHelper mDBHelper;

    public RateDayAdapter(MainActivity activity, int resource, DateTime date) {
        super(activity, resource);
        this.activity = activity;
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.date = date;
        activity.selectedColor = new HashMap<>();
        for (String s : mDBHelper.getFactorList()){
            activity.selectedColor.put(s,1);
        }
    }


    @Override
    public int getCount() {
        return mDBHelper.getFactorList().size();
    }

    @Override
    public String getItem(int position) {
        return mDBHelper.getFactorList().get(position);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView[] rateViews = new TextView[5];

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newRow = inflater.inflate(R.layout.rate_day_layout, null);

        FeedReaderDBHelper dbHelper = FeedReaderDBHelper.getInstance();
        DayEntry entry = dbHelper.getDatabaseEntriesDay(mDBHelper.getFactorList(), date.getDay(), date.getMonth(), date.getYear());
        int rating = 1;
        if (entry != null) {
            HashMap<String, Integer> ratings = entry.ratings;
            rating = ratings.get(getItem(position));
        }


        if(!mDBHelper.getFactorIsGradualMap().get(getItem(position))){
            final Switch mSwitch = (Switch) newRow.findViewById(R.id.rateSwitch);
            TableRow row = (TableRow) newRow.findViewById(R.id.gradualTable);
            row.setVisibility(View.GONE);
            boolean checked = rating == 5;
            mSwitch.setText(getItem(position));
            mSwitch.setChecked(checked);
            mSwitch.setVisibility(View.VISIBLE);
            final int color = activity.getResources().getColor(RatingToColorHelper.ratingToColor(getItem(position), 3));
            mSwitch.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            mSwitch.setOnCheckedChangeListener(new RateViewListener());

        }else {
            TableRow row = (TableRow) newRow.findViewById(R.id.gradualTable);
            row.setVisibility(View.VISIBLE);
            final Switch mSwitch = (Switch) newRow.findViewById(R.id.rateSwitch);
            mSwitch.setVisibility(View.GONE);
            TextView factorText = (TextView) newRow.findViewById(R.id.factorText);
            factorText.setText(getItem(position));

            rateViews[0] = (TextView) newRow.findViewById(R.id.ratText1);
            rateViews[1] = (TextView) newRow.findViewById(R.id.ratText2);
            rateViews[2] = (TextView) newRow.findViewById(R.id.ratText3);
            rateViews[3] = (TextView) newRow.findViewById(R.id.ratText4);
            rateViews[4] = (TextView) newRow.findViewById(R.id.ratText5);


            String color = mDBHelper.getFactorColorMap().get(getItem(position));
            int[] colors = ColorsToPick.getColorByString(color).getAllColors();

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() / 8;
            int height = display.getWidth() / 8;

            for (int i = 0; i < colors.length && i < rateViews.length; i++) {
                ViewGroup.LayoutParams params = rateViews[i].getLayoutParams();
                params.width = width;
                params.height = height;
                rateViews[i].setLayoutParams(params);

                GradientDrawable gd = (GradientDrawable) rateViews[i].getBackground();
                gd.setColor(newRow.getResources().getColor(colors[i]));
                gd.setStroke(2, activity.getResources().getColor(R.color.transparent));
                rateViews[i].setOnClickListener(
                        new RateViewListener(rateViews[i], rateViews, position, i));
            }

            GradientDrawable gd = (GradientDrawable) rateViews[(rating == 0) ? 0 : (rating - 1)].getBackground();
            gd.setStroke(5, activity.getResources().getColor(R.color.black));
            activity.selectedColor.put(getItem(position), rating);
        }
        return newRow;
    }

   public class RateViewListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

       private TextView textView;
       private TextView[] textViews;
       private int factorPosition, index;

       public RateViewListener(TextView textView, TextView[] textViews, int factorPosition, int index){
           this.textView = textView;
           this.textViews = textViews;
           this.factorPosition = factorPosition;
           this.index = index;
       }

       public RateViewListener(){
       }

       @Override
       public void onClick(View v) {
           int oldIndex =  activity.selectedColor.get(getItem(factorPosition))-1;
           oldIndex = (oldIndex == -1) ? 0 : oldIndex;
           GradientDrawable gd = (GradientDrawable)  textViews[oldIndex].getBackground();
           gd.setStroke(1, activity.getResources().getColor(R.color.transparent));

           activity.selectedColor.put(getItem(factorPosition), index+1);
           GradientDrawable gdNew = (GradientDrawable)  textView.getBackground();
           gdNew.setStroke(7, activity.getResources().getColor(R.color.black));
       }

       @Override
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           int i = (isChecked) ? 5 : 1;
           activity.selectedColor.put(buttonView.getText().toString(), i);
       }
   }
}
