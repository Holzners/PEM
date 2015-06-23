package com.team3.pem.pem.view;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.SwitchSymptom;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * Activities containing this fragment MUST implement the {@link com.team3.pem.pem.view.SwitchFragment.SwitchFragmentInterface}.
 */
public class SwitchFragment extends ListFragment {

    private List<SwitchSymptom> symptomList;
    /** The fragment's ListView/GridView.  */
    private ListView mListView;
    /** The Adapter which will be used to populate the ListView/GridView with Views. */
    private ListAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        symptomList = new ArrayList<SwitchSymptom>();
        Resources resources = getResources();
        symptomList.add(new SwitchSymptom(resources.getString(R.string.hello_world)));
        mAdapter = new SwitchFragmentAdapter(getActivity(), symptomList);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SwitchSymptom item = symptomList.get(position);
        Toast.makeText(getActivity(), item.getSymptomName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
     interface SwitchFragmentInterface {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }

}
