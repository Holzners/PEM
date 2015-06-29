/**package com.team3.pem.pem.view;
 *


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team3.pem.pem.R;

public class YearFragment extends Fragment {

    private int selectedYear;

    public YearFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     /***   weekPicker = (Spinner) getView().findViewById(R.id.spinner);
        List<Integer> weeks = new ArrayList<>();
        for(int i = 1 ; i <= 52 ; i++){
            weeks.add(i);
        }
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,weeks);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekPicker.setAdapter(spinnerAdapter);
        weekPicker.setSelection(calenderWeek - 1);
        weekPicker.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position + 1 < calenderWeek) {
                            previousWeek(calenderWeek - position - 1);
                            calenderWeek = position + 1;
                        } else if (position + 1 > calenderWeek) {
                            nextWeek(position + 1 -calenderWeek);
                            calenderWeek = position + 1;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        List<String> factors = new ArrayList<>();
        factors.add("");
        for (Map.Entry<String, String> e : factorColorMap.entrySet()) {
            factors.add(e.getKey());
        }
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        adapter = new WeekViewAdapter((MainActivity)getActivity(),0,factors, displayWidth);
        setListAdapter(adapter);
      *
    }

}
*/