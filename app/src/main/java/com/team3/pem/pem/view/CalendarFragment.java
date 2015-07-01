package com.team3.pem.pem.view;

import android.os.Bundle;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.CalendarFragmentAdapter;

public class CalendarFragment extends CaldroidFragment {

    public CalendarFragment(){
        super();
        this.enableSwipe =false;
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark);
        this.setArguments(args);
    }


    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CalendarFragmentAdapter((MainActivity) getActivity(), month, year, getCaldroidData(), extraData);

    }

    public void notifyAdapter(){
        refreshView();
    }

}
