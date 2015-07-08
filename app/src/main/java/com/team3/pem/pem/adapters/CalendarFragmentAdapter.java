package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
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
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 12.06.15.
 */
public class CalendarFragmentAdapter extends CaldroidGridAdapter{

    FeedReaderDBHelper mDBHelper;
    MainActivity context;

    public CalendarFragmentAdapter(MainActivity context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        DateTime dateTime = this.datetimeList.get(position);
        List<String> copyOfFactors = new ArrayList<>();

        for(String s: mDBHelper.getFactorList()){
            if(mDBHelper.getFactorEnabledMap().get(s)){
                copyOfFactors.add(s);
            }
        }


        View cellView = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.row_month_layout, null);
        }


        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        if(dateTime.getMonth() == this.month) {

            DayEntry dayEntry = mDBHelper.getDatabaseEntriesDay(copyOfFactors, dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());
            TextView colorView[];

                colorView = new TextView[4];
                colorView[0] = (TextView) cellView.findViewById(R.id.tvTopLeft);
                colorView[1] = (TextView) cellView.findViewById(R.id.tvTopRight);
                colorView[2] = (TextView) cellView.findViewById(R.id.tvBottomLeft);
                colorView[3] = (TextView) cellView.findViewById(R.id.tvBottomRight);

            if (dayEntry != null ) {
                if(dayEntry.description != null && !dayEntry.description.equals("")){
                    ImageView imgView = (ImageView) cellView.findViewById(R.id.imageStarView);
                    imgView.setVisibility(View.VISIBLE);
                }
                if(copyOfFactors.size()== 1){
                    for(TextView t : colorView){
                       t.setBackgroundColor(cellView.getResources().getColor(
                                RatingToColorHelper.ratingToColor(copyOfFactors.get(0),
                                        dayEntry.ratings.get(copyOfFactors.get(0)))));
                    }
                }else if (copyOfFactors.size()== 2) {
                    colorView[0].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(0),
                                    dayEntry.ratings.get(copyOfFactors.get(0)))));
                    colorView[1].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(0),
                                    dayEntry.ratings.get(copyOfFactors.get(0)))));
                    colorView[2].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(1),
                                    dayEntry.ratings.get(copyOfFactors.get(1)))));
                    colorView[3].setBackgroundColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(copyOfFactors.get(1),
                                    dayEntry.ratings.get(copyOfFactors.get(1)))));

                }else{
                        for (int i = 0; i < colorView.length && i < dayEntry.ratings.size(); i++) {
                            colorView[i].setBackgroundResource(R.drawable.border);
                            GradientDrawable drawable = (GradientDrawable) colorView[i].getBackground();
                            drawable.setColor(cellView.getResources().getColor(
                                    RatingToColorHelper.ratingToColor(copyOfFactors.get(i),
                                            dayEntry.ratings.get(copyOfFactors.get(i)))));
                        }
                    }
            }


            if(dateTime.isSameDayAs(DateTime.today(TimeZone.getDefault()))){
                TableLayout tableLayout = (TableLayout) cellView.findViewById(R.id.cellContainer);
                tableLayout.setBackgroundResource(R.drawable.border_red);
                GradientDrawable gd = (GradientDrawable) tableLayout.getBackground();
                gd.setColor(cellView.getResources().getColor(R.color.white));
            }

        }
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);
        cellView.setOnClickListener(new CellViewOnClickListener(context, dateTime));
        return cellView;
    }

    public class CellViewOnClickListener implements View.OnClickListener {

        private DateTime date;
        private MainActivity context;

        public CellViewOnClickListener(MainActivity context, DateTime date){
            this.date = date;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            context.showDetailDay(date);
        }
    }
}
