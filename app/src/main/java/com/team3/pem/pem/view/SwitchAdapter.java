package com.team3.pem.pem.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.team3.pem.pem.SwitchSymptom;
import com.team3.pem.pem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Susanne on 15.06.2015.
 */
public class SwitchAdapter extends ArrayAdapter<SwitchSymptom> {

    private Context context;
    private Switch mSwitch;
    private ArrayList<SwitchSymptom> arrayList;
    /** Listen- oder Gridansicht */
    private boolean useList = true;


    public SwitchAdapter(Context context, List<SwitchSymptom> items) {
        super(context, R.layout.switch_row_layout, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View viewToUse;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.switch_row_layout, viewGroup, false);

        if (convertView == null) {
            if (useList) {
                viewToUse = inflater.inflate(R.layout.fragment_switch_list, null);
            } else {
                viewToUse = inflater.inflate(R.layout.fragment_switch_grid, null);
            }
            SwitchSymptom symptom = getItem(position);
            mSwitch = (Switch) viewToUse.findViewById(R.id.switch1);
            mSwitch.setText(symptom.getSymptomName());
        } else {
            viewToUse = convertView;
        }
        return viewToUse;
    }
}