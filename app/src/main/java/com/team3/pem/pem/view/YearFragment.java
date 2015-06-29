package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.YearAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class YearFragment extends ListFragment {

    private int selectedYear;
    private Spinner yearSpinner;



    private HashMap<String ,String> factorColorMap;
    private List<Integer> years;
    public YearFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        yearSpinner = (Spinner) getView().findViewById(R.id.spinner);
        years = new ArrayList<>();
        DateTime today = DateTime.today(TimeZone.getDefault());
        for(int i = 2010 ; i <= today.getYear() ; i++){
            years.add(i);
        }
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, years);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(spinnerAdapter);
        yearSpinner.setSelection(years.indexOf(selectedYear));
        yearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (years.get(position) < selectedYear) {
                            previousYear(selectedYear - years.get(position));
                            selectedYear =years.get(position);
                        } else if (years.get(position)> selectedYear) {
                            nextYear(years.get(position) - selectedYear);
                            selectedYear = years.get(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        YearAdapter adapter = new YearAdapter((MainActivity)getActivity(), 0, displayWidth, factorColorMap);
        this.setListAdapter(adapter);
    }

    private void previousYear(int years){

    }
    private void nextYear(int years){

    }
    public HashMap<String, String> getFactorColorMap() {
        return factorColorMap;
    }

    public void setFactorColorMap(HashMap<String, String> factorColorMap) {
        this.factorColorMap = factorColorMap;
    }

}
