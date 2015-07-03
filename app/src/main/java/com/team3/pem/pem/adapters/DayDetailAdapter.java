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
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.HashMap;

/**
 * Created by Stephan on 01.07.15.
 */
public class DayDetailAdapter extends ArrayAdapter {

    HashMap<String, Integer> factorRatingMap;

    FeedReaderDBHelper mDBHelper;

    public DayDetailAdapter(Context context, int resource, HashMap<String, Integer> factorRatingMap) {
        super(context, resource);
        this.factorRatingMap = factorRatingMap;
        this.mDBHelper = FeedReaderDBHelper.getInstance();
    }

    @Override
    public int getCount() {
        return mDBHelper.getFactorList().size();
    }

    @Override
    public String getItem(int position) {
        return mDBHelper.getFactorList().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(R.layout.day_detail_item, null);

        String item = getItem(position);
        TextView colorText = (TextView) newRow.findViewById(R.id.colorText);
        TextView symptomText = (TextView) newRow.findViewById(R.id.symptomText);
        symptomText.setText(getItem(position));
        colorText.setBackgroundResource(R.drawable.border);
        GradientDrawable gd = (GradientDrawable)colorText.getBackground();
        if(factorRatingMap.containsKey(item)){
            int integer = factorRatingMap.get(item);
            int color = RatingToColorHelper.ratingToColor(item, integer);
            Log.i("ITEM + COLOR", item + " " + Integer.toHexString(color) + " " + integer);
            gd.setColor(color);
        }
        else gd.setColor(RatingToColorHelper.ratingToColor(getItem(position),0));
        return newRow;
    }

    public HashMap<String, Integer> getFactorRatingMap() {
        return factorRatingMap;
    }

    public void setFactorRatingMap(HashMap<String, Integer> factorRatingMap) {
        this.factorRatingMap = factorRatingMap;
    }

    public FeedReaderDBHelper getmDBHelper() {
        return mDBHelper;
    }

    public void setmDBHelper(FeedReaderDBHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }
}
