package com.team3.pem.pem.view;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team3.pem.pem.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekFragment extends ListFragment{

    private int year;
    private int calenderWeek;

    private HashMap<String,Integer> factorColorMap;

    public WeekFragment() {

        // Required empty public constructor
    }

    public void init(){
        factorColorMap = new HashMap<>();
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

    public void updateTable(){

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> factors = new ArrayList<>();
        factors.add("");
        for (Map.Entry<String, Integer> e : factorColorMap.entrySet()){
            factors.add(e.getKey());
        }
        WeekViewAdapter adapter = new WeekViewAdapter(getActivity(),0,factors);
        setListAdapter(adapter);
    }


    public HashMap<String, Integer> getFactorColorMap() {
        return factorColorMap;
    }

    public void setFactorColorMap(HashMap<String, Integer> factorColorMap) {
        this.factorColorMap = factorColorMap;
        updateTable();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCalenderWeek() {
        return calenderWeek;
    }

    public void setCalenderWeek(int calenderWeek) {
        this.calenderWeek = calenderWeek;
    }
}
