package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.DayDetailAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * */
public class DayDetailFragment extends DialogFragment {

    private DateTime selectedDate;
    TextView descriptionDay, weatherText;
    DayDetailAdapter adapter;
    private FeedReaderDBHelper mDBHelper;

    public static DayDetailFragment newInstance(DateTime selectedDate) {
        DayDetailFragment fragment = new DayDetailFragment();
        fragment.selectedDate = selectedDate;
        fragment.mDBHelper = FeedReaderDBHelper.getInstance();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setTitle(selectedDate.format("DD.MM.YYYY"));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.day_detail_fragment, container, false);

        weatherText = (TextView) view.findViewById(R.id.weatherText);
        weatherText.setText(getResources().getString(R.string.weather) +"\n "+ getResources().getString(R.string.noDataAvailable));

        descriptionDay = (TextView)view.findViewById(R.id.descriptionText);
        HashMap<String, Integer> ratings = new HashMap<>();
        DayEntry entry =  mDBHelper.getDatabaseEntriesDay(mDBHelper.getFactorList(), selectedDate.getDay() , selectedDate.getMonth() ,selectedDate.getYear());
        if (entry != null) {
            ratings = entry.ratings;
            descriptionDay.setText(entry.description);
        }
        String s = mDBHelper.getWeatherData(selectedDate);
        if(s != null && !s.equals("")) weatherText.setText(getResources().getString(R.string.weather) + "\n "+ s);
        adapter = new DayDetailAdapter(getActivity(),0,ratings);
        ListView lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(adapter);

        return view;
    }
}
