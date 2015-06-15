package com.team3.pem.pem.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.team3.pem.pem.R;
import com.team3.pem.pem.SymptomFactor;

import java.util.ArrayList;

/**
 * Created by Susanne on 15.06.2015.
 */
public class SwitchAdapter extends ArrayAdapter<SymptomFactor> {

    private Switch mSwitch;
    private ArrayList<SymptomFactor> arrayList;


    public SwitchAdapter(Context context, ArrayList<SymptomFactor> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }

    public View getView (int position, View view, ViewGroup viewGroup) {
        SymptomFactor symptomFactor = getItem(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.switch_row_layout, viewGroup, false);
        }
        mSwitch = (Switch) view.findViewById(R.id.switch1);
        mSwitch.setText(symptomFactor.name);
        return view;
    }

    public Switch getSwitch(){
        return mSwitch;
    }

// ------------------- SwitchFragment   ------------------------------

}
