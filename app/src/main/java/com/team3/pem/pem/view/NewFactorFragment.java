package com.team3.pem.pem.view;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ColorsToPick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephan on 01.07.15.
 */
public class NewFactorFragment extends DialogFragment {
    private EditText newFactorText;
    private FeedReaderDBHelper mDBHelper;
    private String selectedColor = "";
    private TableRow tableRow;
    private MainActivity context;
    private List<String> colors;
    private int selectedIndex = -1;
    private String factor;
    private TextView[] tvs;
    public NewFactorFragment() {

    }

    public static NewFactorFragment getInstance(MainActivity context, String factor, String color ) {
        NewFactorFragment newFactorFragment = new NewFactorFragment();
        newFactorFragment.context = context;
        newFactorFragment.mDBHelper = FeedReaderDBHelper.getInstance();
        if(factor!= null && color != null){
            newFactorFragment.selectedColor = color;
            newFactorFragment.factor = factor;
        }

        return newFactorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_new_factor, container, false);

        mDBHelper = FeedReaderDBHelper.getInstance();
        HashMap<String, String> factorEntries = mDBHelper.getFactorColorMap();

        Button saveButton = (Button) view.findViewById(R.id.button_save_factor);
        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        newFactorText = (EditText) view.findViewById(R.id.inputText);
        TextView tooManySymptomsText = (TextView) view.findViewById(R.id.tooManySymptomsText);
        tooManySymptomsText.setVisibility(View.INVISIBLE);
        tableRow = (TableRow) view.findViewById(R.id.tableColorContainer);

        if (factorEntries.size() == ColorsToPick.values().length && factor == null) {
            tooManySymptomsText.setVisibility(View.VISIBLE);
            tableRow.setVisibility(View.INVISIBLE);
            newFactorText.setVisibility(View.INVISIBLE);
            saveButton.setEnabled(false);
            cancelButton.setEnabled(true);
            newFactorText.setEnabled(false);
        } else {
            colors = new ArrayList<>();
            for (int i = 0; i < ColorsToPick.values().length && colors.size() < (ColorsToPick.values().length - factorEntries.size()); i++) {
                if (!factorEntries.containsValue(ColorsToPick.values()[i].name())) {
                    colors.add(ColorsToPick.values()[i].name());
                } else {
                    Log.d("Color vergeben", ColorsToPick.values()[i].name());
                }
            }
            if(selectedColor != null && factor != null){
                newFactorText.setText(factor);
                newFactorText.setEnabled(false);
                colors.add(selectedColor);
                selectedIndex = colors.indexOf(selectedColor);
            }
            initTable();
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFactor();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    
    public void saveFactor() {
        if (!newFactorText.getText().toString().equals("") && !selectedColor.equals("") ) {
            mDBHelper.saveFactor(newFactorText.getText().toString(), selectedColor);
            context.selectedColor.put(newFactorText.getText().toString(), 1);
            context.refreshAdapters();
            this.dismiss();
        } else {
            Toast.makeText(context, getResources().getString(R.string.noInput), Toast.LENGTH_LONG).show();
        }
    }

    public void initTable(){
        tvs = new TextView[ColorsToPick.values().length];
        int i;
        for(i = 0; i < tvs.length; i++){
            tvs[i] = new TextView(getActivity());

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int size =  display.getWidth()/(ColorsToPick.values().length+2);
            tvs[i].setLayoutParams(new TableRow.LayoutParams(size, size));

            GradientDrawable gd = new GradientDrawable();
            if(i < colors.size()){
                gd.setColor(getActivity().getResources().getColor(ColorsToPick.getColorByString(colors.get(i)).getColor3()));
                tvs[i].setOnClickListener(new OnColorClickListener(i));

            }
            else gd.setColor(getResources().getColor(R.color.bright_foreground_disabled_material_light));

            if(i == selectedIndex) gd.setStroke(2, getActivity().getResources().getColor(R.color.black));
            else gd.setStroke(2, getActivity().getResources().getColor(R.color.transparent));
            tvs[i].setBackground(gd);
            tableRow.addView(tvs[i]);
        }
    }

    private class OnColorClickListener implements View.OnClickListener{

       private  int indexElement;

        private OnColorClickListener(int i){
            indexElement = i;
        }

        @Override
        public void onClick(View v) {
            int oldI = NewFactorFragment.this.selectedIndex;
            if(oldI > -1 && oldI != this.indexElement) {
                GradientDrawable gdOld = (GradientDrawable) tvs[oldI].getBackground();
                gdOld.setStroke(2, getActivity().getResources().getColor(R.color.transparent));
            }
                NewFactorFragment.this.selectedIndex = indexElement;
                NewFactorFragment.this.selectedColor = colors.get(indexElement);
            Log.d("Index", indexElement + " " + selectedColor);

            GradientDrawable gd =  (GradientDrawable) tvs[indexElement].getBackground();
            gd.setColor(getActivity().getResources().getColor(ColorsToPick.getColorByString(colors.get(indexElement)).getColor3()));
            gd.setStroke(2, getActivity().getResources().getColor(R.color.black));


        }
    }
}
