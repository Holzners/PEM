package com.team3.pem.pem.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;

import java.util.ArrayList;
import java.util.Date;
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
        List<String> factors = new ArrayList<>();
        factors = mDBHelper.getFactors();
        HashMap<Date, DayEntry> entryMap = mDBHelper.getDatabaseEntriesDay(factors, dateTime.getDay(), dateTime.getMonth(), dateTime.getYear());

        Date date = new Date(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());

        DayEntry dayEntry = entryMap.get(date);

        LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
            if (convertView == null) {
                cellView = inflater.inflate(R.layout.cell_view_layout, null);
            }
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView imgView1 = (TextView) cellView.findViewById(R.id.textView);
        TextView imgView2 = (TextView) cellView.findViewById(R.id.textView2);
        TextView imgView3 = (TextView) cellView.findViewById(R.id.textView3);
        TextView imgView4 = (TextView) cellView.findViewById(R.id.textView4);

        if(dayEntry != null) {
            Log.d("Database entry for", date +"");

            imgView1.setBackgroundResource(R.drawable.border);
            imgView2.setBackgroundResource(R.drawable.border);
            imgView3.setBackgroundResource(R.drawable.border);
            imgView4.setBackgroundResource(R.drawable.border);

            GradientDrawable drawable1 = (GradientDrawable) imgView1.getBackground();
            GradientDrawable drawable2 = (GradientDrawable) imgView2.getBackground();
            GradientDrawable drawable3 = (GradientDrawable) imgView3.getBackground();
            GradientDrawable drawable4 = (GradientDrawable) imgView4.getBackground();
//            drawable1.setColor(cellView.getResources().getColor(dayEntry.ratings.get(0)));
//            drawable2.setColor(cellView.getResources().getColor(dayEntry.ratings.get(1)));
//            drawable3.setColor(cellView.getResources().getColor(R.color.transparent));
//            drawable4.setColor(cellView.getResources().getColor(R.color.transparent));
        }

        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        return cellView;
    }
}
