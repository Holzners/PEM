package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 12.06.15.
 */
public class CalendarFragmentAdapter extends CaldroidGridAdapter{

    FeedReaderDBHelper mDBHelper;
    List<String> factors;
    MainActivity context;

    public CalendarFragmentAdapter(MainActivity context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.context = context;

        factors = mDBHelper.getFactors();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        DateTime dateTime = this.datetimeList.get(position);

        List<String> copyOfFactors = new ArrayList<>();

        for(String s: factors){
            if(context.getFactorsEnabledMap().get(s)){
                copyOfFactors.add(s);
            }
        }


        View cellView = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.cell_view_layout, null);
        }


        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        if(dateTime.getMonth() == this.month && copyOfFactors.size()>0) {

            DayEntry dayEntry = mDBHelper.getDatabaseEntriesDay(copyOfFactors, dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());
            TextView colorView[];

                colorView = new TextView[4];
                colorView[0] = (TextView) cellView.findViewById(R.id.textView);
                colorView[1] = (TextView) cellView.findViewById(R.id.textView2);
                colorView[2] = (TextView) cellView.findViewById(R.id.textView3);
                colorView[3] = (TextView) cellView.findViewById(R.id.textView4);

                TextView textView = (TextView) cellView.findViewById(R.id.textView5);


            if (dayEntry != null ) {
                if(copyOfFactors.size()== 1){
                    cellView.setBackgroundResource(R.drawable.border);
                    GradientDrawable drawable = (GradientDrawable) cellView.getBackground();
                    drawable.setColor(cellView.getResources().getColor(
                                    RatingToColorHelper.ratingToColor(copyOfFactors.get(0), dayEntry.ratings.get(copyOfFactors.get(0)))));
                }else if (copyOfFactors.size()== 2) {
                    colorView[0].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(0), dayEntry.ratings.get(copyOfFactors.get(0)))));
                    colorView[1].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(0), dayEntry.ratings.get(copyOfFactors.get(0)))));
                    colorView[2].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(1), dayEntry.ratings.get(copyOfFactors.get(1)))));
                    colorView[3].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(1), dayEntry.ratings.get(copyOfFactors.get(1)))));
                    textView.setBackgroundResource(R.drawable.border);
                    GradientDrawable drawable = (GradientDrawable) textView.getBackground();
                    drawable.setColor(cellView.getResources().getColor(R.color.transparent));
                    textView.setPadding(leftPadding, topPadding, rightPadding,
                            bottomPadding);
                }else{
                        for (int i = 0; i < colorView.length && i < dayEntry.ratings.size(); i++) {
                            colorView[i].setBackgroundResource(R.drawable.border);
                            GradientDrawable drawable = (GradientDrawable) colorView[i].getBackground();
                            drawable.setColor(cellView.getResources().getColor(
                                    RatingToColorHelper.ratingToColor(copyOfFactors.get(i), dayEntry.ratings.get(copyOfFactors.get(i)))));
                        }
                    }
            }
                cellView.setBackgroundResource(R.drawable.border);
                GradientDrawable drawable = (GradientDrawable) cellView.getBackground();
                drawable.setColor(cellView.getResources().getColor(R.color.white));

        }
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);
        return cellView;
    }
}
