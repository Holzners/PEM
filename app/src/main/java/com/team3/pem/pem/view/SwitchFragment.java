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

/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * Activities containing this fragment MUST implement the {@link com.team3.pem.pem.view.SwitchFragment.SwitchFragmentInterface}.
 */
public class SwitchFragment extends ListFragment implements AdapterView.OnItemClickListener {

  //  protected static List<SwitchSymptom> symptomList = new ArrayList<>();
    private List<String> symptomList = new ArrayList<>();
    private ListAdapter mAdapter;
    private ListView listView;
    private SwitchFragmentInterface switchFragmentInterface;

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
         symptomList = ((MainActivity)getActivity()).getSymptomList();
        //symptomList.add(new SwitchSymptom("Symptom 1", new Switch(getActivity().getApplicationContext())));
        // symptomList.add(new SwitchSymptom("Symptom 2",new Switch(getActivity().getApplicationContext())));
        mAdapter = new SwitchFragmentAdapter(((MainActivity)getActivity()), symptomList, ((MainActivity) getActivity()).getFactorWithColor());
        setListAdapter(mAdapter);
        // listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Log.i("onItemClick", String.valueOf(position));
      //  SwitchSymptom item = symptomList.get(position);
      //  Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
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
