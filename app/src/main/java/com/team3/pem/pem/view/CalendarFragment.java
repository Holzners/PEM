package com.team3.pem.pem.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.team3.pem.pem.R;

public class CalendarFragment extends Fragment {


    private MJCalendarView mjCalendarView;

    private void initCalendar(View view) {
        this.mjCalendarView = new MJCalendarView(getActivity().getApplicationContext());
        ((FrameLayout)view.findViewById(R.id.contentPanel)).addView(mjCalendarView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        initCalendar(view);
        return view;
    }
}
