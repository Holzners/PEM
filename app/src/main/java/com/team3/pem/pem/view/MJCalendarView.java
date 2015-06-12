package com.team3.pem.pem.view;

import android.content.Context;
import android.widget.CalendarView;
import android.widget.Toast;

import com.team3.pem.pem.R;

/**
 * Created by Stephan on 12.06.15.
 */
public class MJCalendarView extends CalendarView {

    public MJCalendarView(final Context context) {
        super(context);

        setShowWeekNumber(false);
        setFirstDayOfWeek(2);

        setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
        setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Toast.makeText(context, day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

            }


        });

    }

}
