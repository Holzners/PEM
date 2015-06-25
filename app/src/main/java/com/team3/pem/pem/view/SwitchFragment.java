package com.team3.pem.pem.view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.SwitchSymptom;
import com.team3.pem.pem.view.adapters.SwitchFragmentAdapter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * Activities containing this fragment MUST implement the {@link com.team3.pem.pem.view.SwitchFragment.SwitchFragmentInterface}.
 */
public class SwitchFragment extends ListFragment implements AdapterView.OnItemClickListener {

    protected static ArrayList<SwitchSymptom> symptomList = new ArrayList<>();
    private ListAdapter mAdapter;
    private ListView listView;
    private SwitchFragmentInterface switchFragmentInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("SwitchFragment", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SwitchFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_switch_list, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        symptomList.add(new SwitchSymptom("Symptom 1",new Switch(getActivity().getApplicationContext())));
        symptomList.add(new SwitchSymptom("Symptom 2",new Switch(getActivity().getApplicationContext())));
        mAdapter = new SwitchFragmentAdapter(getActivity(), symptomList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("onItemClick", String.valueOf(position));
        SwitchSymptom item = symptomList.get(position);
        Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SwitchFragmentInterface){
           switchFragmentInterface = (SwitchFragmentInterface) activity;
        }
        else throw new ClassCastException(activity.toString()+ " must implement SwitchFragmentInterface");
    }

    public interface SwitchFragmentInterface {
        void updateSymptoms();
    }

}
