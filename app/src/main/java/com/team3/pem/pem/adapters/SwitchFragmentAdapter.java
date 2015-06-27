package com.team3.pem.pem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;

import java.util.HashMap;
import java.util.List;

public class SwitchFragmentAdapter extends ArrayAdapter<String> {

  /**  private List<SwitchSymptom> arrayList;
    private Switch mSwitch;

    public SwitchFragmentAdapter(Context context, List<SwitchSymptom> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
    }
*/
    private HashMap<String,String> factorWithColor;
    private List<String> factors;
    private MainActivity context;

  public SwitchFragmentAdapter(MainActivity context, List<String> factors , HashMap<String,String> factorWithColor) {
      super(context,0,factors);
      this.factorWithColor = factorWithColor;
      this.factors = factors;
      this.context =context;
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
      /**  SwitchSymptom switchSymptom = getItem(position);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.switch_row_layout, viewGroup, false);
        }
        mSwitch = (Switch) view.findViewById(R.id.switch1);
        mSwitch.setText(switchSymptom.name);
//        mSwitch.setColor(switchSymptom.color);

        return view;
       */
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newView = inflater.inflate(R.layout.switch_row_layout, null);

        Switch mSwitch = (Switch) newView.findViewById(R.id.switch1);
        mSwitch.setText(factors.get(position));

        mSwitch.setOnClickListener(new SwitchOnClickListener(mSwitch, factors.get(position)));
        return newView;
    }

    public class SwitchOnClickListener implements View.OnClickListener{

        private Switch mSwitch;

        private String symptom;

        public SwitchOnClickListener(Switch mSwitch, String symptom) {
            this.mSwitch = mSwitch;
            this.symptom = symptom;
        }

        @Override
        public void onClick(View v) {
            context.switchSymptom(mSwitch.isChecked() , symptom);
            Toast.makeText(context, symptom + " enabled " + mSwitch.isChecked(), Toast.LENGTH_SHORT).show();
        }
    }
}