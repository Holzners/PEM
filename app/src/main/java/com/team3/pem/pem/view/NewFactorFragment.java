package com.team3.pem.pem.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ColorsToPick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 01.07.15.
 */
public class NewFactorFragment extends DialogFragment {
    EditText newFactorText;
    TextView toManySymptomsText;
    Button FAB;
    Button deleteButton;
    FeedReaderDBHelper mDBHelper;
    Spinner colorSpinner;
    String selectedColor;

    MainActivity context;

    public NewFactorFragment() {

    }

    public static NewFactorFragment getInstance(MainActivity context) {
        NewFactorFragment newFactorFragment = new NewFactorFragment();
        newFactorFragment.context = context;
        newFactorFragment.mDBHelper = FeedReaderDBHelper.getInstance();
        return newFactorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_new_factor, container, false);

        mDBHelper = FeedReaderDBHelper.getInstance();
        FAB = (Button) view.findViewById(R.id.FAB);
        deleteButton = (Button) view.findViewById(R.id.delete_button);
        newFactorText = (EditText) view.findViewById(R.id.inputText);
        toManySymptomsText = (TextView) view.findViewById(R.id.tooManySymptomsText);
        toManySymptomsText.setVisibility(View.INVISIBLE);

        HashMap<String, String> factorEntries = mDBHelper.getFactorsFromDatabase();

        if (factorEntries.size() == ColorsToPick.values().length) {
            toManySymptomsText.setVisibility(View.VISIBLE);
            FAB.setEnabled(false);
            deleteButton.setEnabled(false);
            newFactorText.setEnabled(false);
        } else {
            List<String> colors = new ArrayList<>();
            for (int i = 0; i < ColorsToPick.values().length && colors.size() < (ColorsToPick.values().length - factorEntries.size()); i++) {
                if (!factorEntries.containsValue(ColorsToPick.values()[i].name())) {
                    colors.add(ColorsToPick.values()[i].name());
                } else {
                    Log.d("Color vergeben", ColorsToPick.values()[i].name());
                }
            }


            colorSpinner = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, colors);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorSpinner.setAdapter(dataAdapter);

            colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedColor = parent.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFactor();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFactor();
            }

        });
        return view;
    }




    public void saveFactor() {
        if (!newFactorText.getText().toString().equals("")) {
            mDBHelper.saveFactor(newFactorText.getText().toString(), selectedColor);
            context.selectedColor.put(newFactorText.getText().toString(), 1);
            context.refreshAdapters();
            this.dismiss();
            context.showRateDayPopup(DateTime.today(TimeZone.getDefault()));
        } else {
            Toast.makeText(context, getResources().getString(R.string.noInput), Toast.LENGTH_LONG).show();
        }
    }

    public void removeFactor(){
        context.showRemoveFactorDialog();
    }


}
