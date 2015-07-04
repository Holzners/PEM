package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.RateDayAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.DayEntry;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 01.07.15.
 */
public class RateDayFragment extends DialogFragment {

    DateTime date;
    MainActivity context;
    FeedReaderDBHelper mDBHelper;

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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_rate_day, container, false);
        ListView lv = (ListView) view.findViewById(R.id.list);
        RateDayAdapter adapter = new RateDayAdapter(context, R.layout.rate_day_layout, date);
        lv.setAdapter(adapter);

//        ImageView newFactor = (ImageView) view.findViewById(R.id.newFactor);
//        newFactor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.showNewFactorDialog(v);
//                RateDayFragment.this.dismiss();
//            }
//        });

        Button saveDay = (Button) view.findViewById(R.id.button_save_day);
        Button cancel = (Button) view.findViewById(R.id.button_cancel);

        final EditText editText = (EditText) view.findViewById(R.id.editNote);
        DayEntry entry = mDBHelper.getDatabaseEntriesDay(mDBHelper.getFactorList(), date.getDay(), date.getMonth(), date.getYear());
        if (entry != null)
            editText.setText(entry.description);

        saveDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Date", date + "");
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
