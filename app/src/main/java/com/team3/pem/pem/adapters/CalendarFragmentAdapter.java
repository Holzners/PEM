package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.Color;
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
 * @author Stephan on 12.06.15.
 */
public class CalendarFragmentAdapter extends CaldroidGridAdapter {

    private FeedReaderDBHelper mDBHelper;
    private MainActivity context;

    public CalendarFragmentAdapter(MainActivity context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get Date of Position
        DateTime dateTime = this.datetimeList.get(position);
        //Get Factors
        List<String> copyOfFactors = new ArrayList<>();
        for (String s : mDBHelper.getFactorList()) {
            if (mDBHelper.getFactorEnabledMap().get(s)) {
                copyOfFactors.add(s);
            }
        }
        // Inflate Layout
        View cellView = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.row_month_layout, null);
        }
        //store old Padding
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        //Check if this Position is in selected Month
        if (dateTime.getMonth() == this.month) {
            //get Day Entries
            DayEntry dayEntry = mDBHelper.getDatabaseEntriesDay(copyOfFactors, dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());
            //Init Textboxes
            TextView colorView[];
            colorView = new TextView[4];
            colorView[0] = (TextView) cellView.findViewById(R.id.tvTopLeft);
            colorView[1] = (TextView) cellView.findViewById(R.id.tvTopRight);
            colorView[2] = (TextView) cellView.findViewById(R.id.tvBottomLeft);
            colorView[3] = (TextView) cellView.findViewById(R.id.tvBottomRight);
            colorView[0].setBackgroundColor(Color.TRANSPARENT);
            colorView[1].setBackgroundColor(Color.TRANSPARENT);
            colorView[2].setBackgroundColor(Color.TRANSPARENT);
            colorView[3].setBackgroundColor(Color.TRANSPARENT);

            if (dayEntry != null) {
                //Check if day has notice
                if (dayEntry.description != null && !dayEntry.description.equals("")) {
                    //Show small image in Cell
                    ImageView imgView = (ImageView) cellView.findViewById(R.id.imageStarView);
                    imgView.setVisibility(View.VISIBLE);
                }
                if (copyOfFactors.size() == 1) {
                    for (TextView t : colorView) {
                        //if only one factor is enabled Fill Cell with same Color
                        t.setBackgroundColor(cellView.getResources().getColor(
                                RatingToColorHelper.ratingToColor(copyOfFactors.get(0),
                                        dayEntry.ratings.get(copyOfFactors.get(0)))));
                    }
                } else if (copyOfFactors.size() == 2) {
                    //if two factors are enabled split Cell in two Section
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

                } else {

                    //if more than two(n) factors are enabled split Cell in n Sections
                    for (int i = 0; i < colorView.length && i < dayEntry.ratings.size(); i++) {
                        colorView[i].setBackgroundResource(R.drawable.border);
                        GradientDrawable drawable = (GradientDrawable) colorView[i].getBackground();
                        drawable.setColor(cellView.getResources().getColor(
                                RatingToColorHelper.ratingToColor(copyOfFactors.get(i),
                                        dayEntry.ratings.get(copyOfFactors.get(i)))));
                    }
                }
            }
            //mark Todays Date with red Stroke
            if (dateTime.isSameDayAs(DateTime.today(TimeZone.getDefault()))) {
                TableLayout tableLayout = (TableLayout) cellView.findViewById(R.id.cellContainer);
                tableLayout.setBackgroundResource(R.drawable.border_red);
                GradientDrawable gd = (GradientDrawable) tableLayout.getBackground();
                gd.setColor(cellView.getResources().getColor(R.color.white));
            }
            cellView.setOnClickListener(new CellViewOnClickListener(context, dateTime));
        } else {
            //Set Cells not in this Month Background transparent
            TableLayout tableLayout = (TableLayout) cellView.findViewById(R.id.cellContainer);
            tableLayout.setBackgroundResource(R.color.transparent);
        }
        //reset old Paddings
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        return cellView;
    }


    public class CellViewOnClickListener implements View.OnClickListener {

        private DateTime date;
        private MainActivity context;

        /**
         * On Click cell Show Clicked Day Details in Popup
         * @param context
         * @param date
         */
        public CellViewOnClickListener(MainActivity context, DateTime date) {
            this.date = date;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            context.showDetailDay(date);
        }
    }
}
