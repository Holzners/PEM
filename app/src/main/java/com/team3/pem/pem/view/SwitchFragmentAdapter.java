package com.team3.pem.pem.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.SwitchSymptom;

import java.util.ArrayList;

public class SwitchFragmentAdapter extends ArrayAdapter<SwitchSymptom> {

    private ArrayList<SwitchSymptom> arrayList;
    private Switch mSwitch;

    public SwitchFragmentAdapter(Context context, ArrayList<SwitchSymptom> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }

    public View getView(final int position, View view, ViewGroup viewGroup) {
        SwitchSymptom switchSymptom = getItem(position);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.switch_row_layout, viewGroup, false);
        }
        mSwitch = (Switch) view.findViewById(R.id.switch1);
        mSwitch.setText(switchSymptom.name);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked on symptom " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
//        mSwitch.setColor(switchSymptom.color);
        return view;
    }
}