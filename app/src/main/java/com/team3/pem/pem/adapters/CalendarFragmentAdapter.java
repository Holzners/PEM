package com.team3.pem.pem.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 12.06.15.
 */
public class CalendarFragmentAdapter extends CaldroidGridAdapter{

    FeedReaderDBHelper mDBHelper;

    public CalendarFragmentAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        this.mDBHelper = FeedReaderDBHelper.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        DateTime dateTime = this.datetimeList.get(position);

        List<String> factors;
        factors = mDBHelper.getFactors();
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

        if(dateTime.getMonth() == this.month) {

            DayEntry dayEntry = mDBHelper.getDatabaseEntriesDay(factors, dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());

            TextView imgView1 = (TextView) cellView.findViewById(R.id.textView);
            TextView imgView2 = (TextView) cellView.findViewById(R.id.textView2);
            TextView imgView3 = (TextView) cellView.findViewById(R.id.textView3);
            TextView imgView4 = (TextView) cellView.findViewById(R.id.textView4);
            TextView textView = (TextView) cellView.findViewById(R.id.textView5);


            if (dayEntry != null) {

                imgView1.setBackgroundResource(R.drawable.border);
                imgView2.setBackgroundResource(R.drawable.border);
                imgView3.setBackgroundResource(R.drawable.border);
                imgView4.setBackgroundResource(R.drawable.border);

                GradientDrawable drawable1 = (GradientDrawable) imgView1.getBackground();
                GradientDrawable drawable2 = (GradientDrawable) imgView2.getBackground();
                GradientDrawable drawable3 = (GradientDrawable) imgView3.getBackground();
                GradientDrawable drawable4 = (GradientDrawable) imgView4.getBackground();
                drawable1.setColor(cellView.getResources().getColor(
                        RatingToColorHelper.ratingToColor(factors.get(0), dayEntry.ratings.get(0))));
                drawable2.setColor(cellView.getResources().getColor(
                        RatingToColorHelper.ratingToColor(factors.get(1), dayEntry.ratings.get(1))));
                try {
                    drawable3.setColor(cellView.getResources().getColor(
                            RatingToColorHelper.ratingToColor(factors.get(2), dayEntry.ratings.get(2))));
                    drawable4.setColor(cellView.getResources().getColor(R.color.white));
                }catch (IndexOutOfBoundsException ie){

                }
            } else {
                cellView.setBackgroundResource(R.drawable.border);
                GradientDrawable drawable = (GradientDrawable) cellView.getBackground();
                drawable.setColor(cellView.getResources().getColor(R.color.white));
                //textView.setText(""+dateTime.getDay());

            }


        }
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);
        return cellView;
    }
}