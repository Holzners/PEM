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
import com.team3.pem.pem.adapters.WeekViewAdapter;

import java.util.Calendar;

public class WeekFragment extends ListFragment{

    private int year;
    private int calenderWeek;
    WeekViewAdapter adapter;
//    Spinner weekPicker;
    private ImageButton previous, next;
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
        adapter = new WeekViewAdapter((MainActivity)getActivity(),0, displayWidth);
        setListAdapter(adapter);

        week = (TextView) getView().findViewById(R.id.week);
        setTitle();
        previous = (ImageButton) getView().findViewById(R.id.previousWeek);
        next = (ImageButton) getView().findViewById(R.id.nextWeek);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeek(calenderWeek);
                setTitle();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeek(calenderWeek);
                setTitle();
            }
        });

//        weekPicker = (Spinner) getView().findViewById(R.id.spinner);
//        List<Integer> weeks = new ArrayList<>();
//        for(int i = 1 ; i <= 52 ; i++){
//            weeks.add(i);
//        }
//        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter(getActivity(),
//                android.R.layout.simple_spinner_item,weeks);
//
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        weekPicker.setAdapter(spinnerAdapter);
//        weekPicker.setSelection(calenderWeek - 1);
//        weekPicker.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (position + 1 < calenderWeek) {
//                            previousWeek(calenderWeek - position - 1);
//                            calenderWeek = position + 1;
//                        } else if (position + 1 > calenderWeek) {
//                            nextWeek(position + 1 -calenderWeek);
//                            calenderWeek = position + 1;
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });


    }


    public void nextWeek(int weeks){
//        calenderWeek += 1;
        adapter.setFirstDayOfSelectedWeek(adapter.getFirstDayOfSelectedWeek().plusDays(7*weeks));
        adapter.notifyDataSetChanged();
    }

    public void previousWeek(int weeks){
//        calenderWeek -= 1;
        adapter.setFirstDayOfSelectedWeek(adapter.getFirstDayOfSelectedWeek().minusDays(7 * weeks));
        adapter.notifyDataSetChanged();
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

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    private String getWeekNr () {
        return String.valueOf(calenderWeek);
    }

    private String getFirstDay() {
        return String.valueOf(adapter.getFirstDayOfSelectedWeek().format("DD.MM.YYYY"));
    }

    private void setTitle(){
        week.setText(getWeekNr());
    }
}
