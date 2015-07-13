package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.RateDayAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;

import hirondelle.date4j.DateTime;

/**
 * @author Stephan on 01.07.15.
 */
public class RateDayFragment extends DialogFragment {

    private DateTime date;
    private MainActivity context;
    private FeedReaderDBHelper mDBHelper;

    public RateDayFragment(){

    }

    public static RateDayFragment getInstance(DateTime date, MainActivity context) {
        RateDayFragment rateDayFragment = new RateDayFragment();
        rateDayFragment.date = date;
        rateDayFragment.context = context;
        rateDayFragment.mDBHelper = FeedReaderDBHelper.getInstance();
        return rateDayFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Dialog Settings
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        //Set Layout
        View view = inflater.inflate(R.layout.dialog_rate_day, container, false);
        //Set ListView and Adapter
        ListView lv = (ListView) view.findViewById(R.id.list);
        RateDayAdapter adapter = new RateDayAdapter(context, R.layout.rate_day_layout, date);
        lv.setAdapter(adapter);

        //Save and Cancle Buttons
        Button saveDay = (Button) view.findViewById(R.id.button_save_day);
        Button cancel = (Button) view.findViewById(R.id.button_cancel);
        //selected Date TextVeiw
        TextView currentDate = (TextView) view.findViewById(R.id.rateDayDate);
        currentDate.setText(date.format("DD.MM.YYYY"));
        //Input Text
        final EditText editText = (EditText) view.findViewById(R.id.editNote);
        DayEntry entry = mDBHelper.getDatabaseEntriesDay(mDBHelper.getFactorList(), date.getDay(), date.getMonth(), date.getYear());
        if (entry != null)
            editText.setText(entry.description);

        //OnClick Listener
        saveDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.saveDay(date, editText.getText().toString());
                RateDayFragment.this.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDayFragment.this.dismiss();
            }
        });
        return view;
    }

}
