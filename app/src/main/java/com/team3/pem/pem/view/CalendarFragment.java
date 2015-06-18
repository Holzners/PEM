package com.team3.pem.pem.view;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.MainActivity;

public class CalendarFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub

        return new CalendarFragmentAdapter(getActivity(), month, year, getCaldroidData(), extraData,  ((MainActivity)getActivity()).mDHelber);
    }

}
