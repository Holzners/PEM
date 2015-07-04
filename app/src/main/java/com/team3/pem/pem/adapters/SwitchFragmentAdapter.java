package com.team3.pem.pem.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.RatingToColorHelper;

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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newView = inflater.inflate(R.layout.row_switch_layout, null);

        Switch mSwitch = (Switch) newView.findViewById(R.id.switch1);
        mSwitch.setText(getItem(position));
        mSwitch.setChecked(mDBHelper.getFactorEnabledMap().get(getItem(position)));

        mSwitch.setOnCheckedChangeListener(new SwitchOnCheckedChangedListener(getItem(position)));
        mSwitch.setText(getItem(position));
        mSwitch.setChecked(mDBHelper.getFactorEnabledMap().get(getItem(position)));
        mSwitch.setOnCheckedChangeListener(new SwitchOnCheckedChangedListener(getItem(position)));

        String string = mDBHelper.getFactorColorMap().get(getItem(position));
        int color = RatingToColorHelper.ratingToColor(string, 3);
        mSwitch.getThumbDrawable().setTint(color);
//        mSwitch.getTrackDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
//        mSwitch.setThumbDrawable(context.getDrawable(R.drawable.abc_btn_radio_material));

        mSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.setContextMenuOn(true);
                context.invalidateOptionsMenu();
                return true;
            }
        });
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

