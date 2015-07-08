package com.team3.pem.pem.view;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.SwitchFragmentAdapter;

public class SwitchFragment extends ListFragment {

    private SwitchFragmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.fragment_switch_list, container, false);
        this.setHasOptionsMenu(true);
        return newView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SwitchFragmentAdapter(((MainActivity)getActivity()));
        setListAdapter(mAdapter);
    }

    public void notifyAdapter(){
        mAdapter.notifyDataSetChanged();
    }
}
