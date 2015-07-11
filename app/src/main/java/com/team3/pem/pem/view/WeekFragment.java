package com.team3.pem.pem.view;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.WeekViewAdapter;

import java.util.Calendar;

import hirondelle.date4j.DateTime;

public class WeekFragment extends ListFragment{

    private int year;
    private int calenderWeek;
    private WeekViewAdapter adapter;
    private TextView week;

    public WeekFragment() {
        init();
        // Required empty public constructor
    }

    public void init(){
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        calenderWeek = c.get(Calendar.WEEK_OF_YEAR);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        adapter = new WeekViewAdapter((MainActivity) getActivity(), 0, displayWidth);
        setListAdapter(adapter);

        week = (TextView) getView().findViewById(R.id.week);
        setTitle();
        ImageButton previous = (ImageButton) getView().findViewById(R.id.previousWeek);
        ImageButton next = (ImageButton) getView().findViewById(R.id.nextWeek);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeek(1);
                setTitle();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeek(1);
                setTitle();
            }
        });
    }

    /**
     * Sets the current week to one week later
     *
     * @param weeks
     */
    public void nextWeek(int weeks){
        calenderWeek += 1;
        adapter.setFirstDayOfSelectedWeek(adapter.getFirstDayOfSelectedWeek().plusDays(7 * weeks));
        adapter.notifyDataSetChanged();
        setTitle();
    }

    /**
     * Sets the current week to one week earlier
     *
     * @param weeks
     */
    public void previousWeek(int weeks){
        calenderWeek -= 1;
        adapter.setFirstDayOfSelectedWeek(adapter.getFirstDayOfSelectedWeek().minusDays(7 * weeks));
        adapter.notifyDataSetChanged();
        setTitle();
    }

    /**
     * Get the current year
     *
     * @return <code>year</code>
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the current year
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Notify current adapter for data changed
     */
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Sets the title of the week to match current week
     */
    private void setTitle(){
        DateTime firstDay = adapter.getFirstDayOfSelectedWeek();
        DateTime lastDay = adapter.lastDayOfThisWeek();
        String sFirstDay = firstDay.format("DD") + ".";
        String sLastDay = lastDay.format("DD.MM.YYYY");
        String monthFirstDay = "";
        String yearFirstDay = "";
        if(firstDay.getMonth() != lastDay.getMonth()) {
            monthFirstDay =  firstDay.format("MM") + ".";
            if (!(firstDay.getYear()+"").equals(lastDay.getYear()+"")) {
                Log.d("Not Equal", firstDay.getYear()+ " " +  lastDay.getYear());
                yearFirstDay = "." + firstDay.format("YYYY");
            }
        }
        week.setText(sFirstDay + monthFirstDay+ yearFirstDay+ " - " +sLastDay);
    }
}
