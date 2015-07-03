package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.YearAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class YearFragment extends ListFragment {

    private int selectedYear;
    private YearAdapter adapter;
    private TextView year;
    private ImageButton previous, next;
    private List<Integer> years;
    public YearFragment() {
        DateTime dateTime = DateTime.today(TimeZone.getDefault());
        selectedYear = dateTime.getYear();
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
//        yearSpinner = (Spinner) getView().findViewById(R.id.spinner);

        year = (TextView) getView().findViewById(R.id.year);
        year.setText(String.valueOf(selectedYear));

        previous = (ImageButton) getView().findViewById(R.id.previousYear);
        next = (ImageButton) getView().findViewById(R.id.nextYear);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeYear(selectedYear-1);
                selectedYear = selectedYear-1;
                year.setText(String.valueOf(selectedYear));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeYear(selectedYear+1);
                selectedYear = selectedYear+1;
                year.setText(String.valueOf(selectedYear));
            }
        });


        years = new ArrayList<>();
        DateTime today = DateTime.today(TimeZone.getDefault());
        for(int i = 2010 ; i <= today.getYear() ; i++){
            years.add(i);
        }
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        adapter = new YearAdapter((MainActivity)getActivity(), 0, displayWidth, selectedYear);
        this.setListAdapter(adapter);
    }

    private void changeYear(int year){
        adapter.setSelectedYear(year);
    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

}
