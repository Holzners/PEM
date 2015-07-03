package com.team3.pem.pem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.ExportActivity;

import java.util.List;

/**
 * Created by Chris on 30.06.2015.
 */
public class SwitchExportAdapter extends ArrayAdapter<String>{

    private ExportActivity context;
    private int resource;
    private List<String> factors;


    public SwitchExportAdapter(ExportActivity context, int resource, List<String> factors) {
        super(context, resource, factors);
        this.factors = factors;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return factors.size();
    }

    @Override
    public String getItem(int position) {
        return factors.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newView = inflater.inflate(R.layout.switch_row_layout, null);

        Switch mSwitch = (Switch) newView.findViewById(R.id.switch1);
        mSwitch.setText(factors.get(position));
        if(context.allChecked) {
            mSwitch.setChecked(true);
        }
        else {
            for(String enabled : context.enabledSymptoms) {
                if(mSwitch.getText().toString().equals(enabled)) {
                    mSwitch.setChecked(true);
                    break;
                }
                else
                    mSwitch.setChecked(false);
            }
        }

        mSwitch.setOnCheckedChangeListener(new SwitchOnCheckedChangedListener(factors.get(position)));
        return newView;
    }

    public class SwitchOnCheckedChangedListener implements CompoundButton.OnCheckedChangeListener{

        private String symptom;

        public SwitchOnCheckedChangedListener(String symptom) {
            this.symptom = symptom;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            context.switchSymptom(isChecked , symptom);
        }
    }
}
