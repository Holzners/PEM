package com.team3.pem.pem.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.RatingToColorHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class SwitchFragmentAdapter extends ArrayAdapter<String> {

    private MainActivity context;
    private FeedReaderDBHelper mDBHelper;

  public SwitchFragmentAdapter(MainActivity context) {
      super(context,0);
      this.context = context;
      mDBHelper = FeedReaderDBHelper.getInstance();
  }

    @Override
    public int getCount() {
        return mDBHelper.getFactorList().size();
    }

    @Override
    public String getItem(int position) {
        return mDBHelper.getFactorList().get(position);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //inflate Layout
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newView = inflater.inflate(R.layout.row_switch_layout, null);

        //get State of Factor and set it to state of Switch
        final Switch mSwitch = (Switch) newView.findViewById(R.id.switch1);
        mSwitch.setText(getItem(position));
        mSwitch.setChecked(mDBHelper.getFactorEnabledMap().get(getItem(position)));
        //set OnChange Listener
        mSwitch.setOnCheckedChangeListener(new SwitchOnCheckedChangedListener(getItem(position)));
        String s = mSwitch.getText().toString();
        final int color = context.getResources().getColor(RatingToColorHelper.ratingToColor(s, 3));
        mSwitch.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        //On Long ClickListener open Remove/ Edit Factor Properties
        mSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mSwitch.setTextColor(color);
                context.setContextMenuOn(true, mSwitch);
                context.invalidateOptionsMenu();
                return true;
            }
        });

        //Set New Factor Button in Last Row
        Button button = (Button) newView.findViewById(R.id.button_add_symptom);
        if (position==getCount()-1 ) button.setVisibility(View.VISIBLE);

        return newView;
    }

    public class SwitchOnCheckedChangedListener implements CompoundButton.OnCheckedChangeListener{

        private String symptom;

        public SwitchOnCheckedChangedListener(String symptom) {
            this.symptom = symptom;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int count = Collections.frequency(new ArrayList<>(mDBHelper.getFactorEnabledMap().values()), true);
            //Check if more than 4 Factors are enabled
            if(count == 4 && isChecked){
                //If yes disable other random Symptom
                Random generator = new Random();
                for(Iterator<Map.Entry<String, Boolean>> it = mDBHelper.getFactorEnabledMap().entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, Boolean> entry = it.next();
                    Boolean n = entry.getValue();
                    if(!n)
                        it.remove();
                }
                Object[] keys = mDBHelper.getFactorEnabledMap().keySet().toArray();
                String randomKey = (String) keys[generator.nextInt(keys.length)];
                mDBHelper.switchFactor(randomKey, false);
            }
            context.switchSymptom(isChecked, symptom);
        }
    }
}

