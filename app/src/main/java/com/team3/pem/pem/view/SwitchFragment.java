package com.team3.pem.pem.view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.SwitchFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchFragment extends ListFragment {

    private ListAdapter mAdapter;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_switch_list, container, false);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SwitchFragmentAdapter(((MainActivity)getActivity()));
        setListAdapter(mAdapter);
    }


}
