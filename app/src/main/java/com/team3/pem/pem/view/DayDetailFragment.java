package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
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
    private FeedReaderDBHelper mDBHelper;

    /**
     * @param selectedDate
     * @return
     */
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.day_detail_fragment, container, false);

        TextView weatherText = (TextView) view.findViewById(R.id.weatherText);
        weatherText.setText(getResources().getString(R.string.weather) + "\n " + getResources().getString(R.string.noDataAvailable));

        TextView descriptionDay = (TextView) view.findViewById(R.id.descriptionText);
        TextView date = (TextView) view.findViewById(R.id.daydetaildate);
        date.setText(selectedDate.format("DD.MM.YYYY"));

        ImageButton edit = (ImageButton) view.findViewById(R.id.editDay);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity context = (MainActivity)getActivity();
                context.showRateDayPopup(selectedDate);
                dismiss();
            }
        });

        HashMap<String, Integer> ratings = new HashMap<>();
        DayEntry entry =  mDBHelper.getDatabaseEntriesDay(mDBHelper.getFactorList(), selectedDate.getDay() , selectedDate.getMonth() ,selectedDate.getYear());
        if (entry != null) {
            ratings = entry.ratings;
            if(!entry.description.equals(""))
                descriptionDay.setText("\"" + entry.description + "\"");
            else
                descriptionDay.setText(getResources().getString(R.string.noDataAvailable));
        }
        String s = mDBHelper.getWeatherData(selectedDate);
        if(s != null && !s.equals("")){
            String[] splitWeather = s.split("\n");
            weatherText.setText(getResources().getString(R.string.weather) + "\n " + splitWeather[0] + "\n" + splitWeather[1] + "\n" + splitWeather[4]);
        }
        DayDetailAdapter adapter = new DayDetailAdapter(getActivity(), 0, ratings);
        ListView lv = (ListView) view.findViewById(R.id.list);
        lv.setAdapter(adapter);

        return view;
    }
}
