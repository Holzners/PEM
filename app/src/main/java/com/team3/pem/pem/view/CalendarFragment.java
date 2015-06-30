package com.team3.pem.pem.view;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.CalendarFragmentAdapter;

public class CalendarFragment extends CaldroidFragment {

    public CalendarFragment(){
        super();
        this.enableSwipe =false;
    }


    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CalendarFragmentAdapter((MainActivity)getActivity(), month , year, getCaldroidData(), extraData);

    }




    public void notifyAdapter(){
        refreshView();
    }

}
