package com.team3.pem.pem.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.adapters.SwitchFragmentAdapter;

public class SwitchFragment extends ListFragment {

    private ListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.fragment_switch_list, container, false);
        FloatingActionButton newFactorRating = (FloatingActionButton) newView.findViewById(R.id.newFactorRating);
        FloatingActionButton newFactor = (FloatingActionButton) newView.findViewById(R.id.newFactor);
        newFactor.setContentDescription("HALLO");
        newFactorRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TEST", "TEST");
            }
        });
        this.setHasOptionsMenu(true);
        return newView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SwitchFragmentAdapter(((MainActivity)getActivity()));
        setListAdapter(mAdapter);
    }
}
