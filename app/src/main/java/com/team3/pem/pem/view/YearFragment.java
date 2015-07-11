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

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class YearFragment extends ListFragment {

    private int selectedYear;
    private YearAdapter adapter;

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

        final TextView year = (TextView) getView().findViewById(R.id.year);
        year.setText(String.valueOf(selectedYear));

        ImageButton previous = (ImageButton) getView().findViewById(R.id.previousYear);
        ImageButton next = (ImageButton) getView().findViewById(R.id.nextYear);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeYear(selectedYear - 1);
                selectedYear = selectedYear - 1;
                year.setText(String.valueOf(selectedYear));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeYear(selectedYear + 1);
                selectedYear = selectedYear + 1;
                year.setText(String.valueOf(selectedYear));
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        adapter = new YearAdapter((MainActivity)getActivity(), 0, displayWidth, selectedYear);
        this.setListAdapter(adapter);
    }

    /**
     * Change the current year
     *
     * @param year
     */
    private void changeYear(int year){
        adapter.setSelectedYear(year);
    }

    /**
     * Notify the adapter for data changed
     */
    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

}
