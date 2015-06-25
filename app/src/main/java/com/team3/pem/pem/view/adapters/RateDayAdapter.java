package com.team3.pem.pem.view.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.utili.ColorsToPick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephan on 25.06.15.
 */
public class RateDayAdapter extends ArrayAdapter implements View.OnClickListener {

    HashMap<String,String> factorColors;

    List<String> factors;

    Context context;

    HashMap<String, Integer> selectedColor;

    public RateDayAdapter(Context context, int resource, HashMap<String,String> factorColors) {
        super(context, resource);
        this.context = context;
        this.factorColors = factorColors;
        factors = new ArrayList<>();
        for (Map.Entry<String,String> e : factorColors.entrySet()){
            factors.add(e.getKey());
        }
        selectedColor = new HashMap<>();
    }
    @Override
    public int getCount() {
        return factors.size();
    }

    @Override
    public String getItem(int position) {
        return factors.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView[] rateViews = new TextView[5];

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newRow = inflater.inflate(R.layout.rate_day_layout, null);

        TextView factorText = (TextView) newRow.findViewById(R.id.factorText);
        factorText.setText(factors.get(position));

        rateViews[0] = (TextView) newRow.findViewById(R.id.ratText1);
        rateViews[1] = (TextView) newRow.findViewById(R.id.ratText2);
        rateViews[2] = (TextView) newRow.findViewById(R.id.ratText3);
        rateViews[3] = (TextView) newRow.findViewById(R.id.ratText4);
        rateViews[4] = (TextView) newRow.findViewById(R.id.ratText5);

        EditText notePad = (EditText) newRow.findViewById(R.id.noteText);

        String color = factorColors.get(factors.get(position));
        int[]colors = ColorsToPick.getColorByString(color).getAllColors();

        for (int i = 0 ; i < colors.length && i < rateViews.length ; i++){

            GradientDrawable gd = (GradientDrawable) rateViews[i].getBackground();
            gd.setColor(newRow.getResources().getColor(colors[i]));
            final int colorFinal = colors[i];
            final TextView tv = rateViews[i];
            rateViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedColor.put(factors.get(position), colorFinal);
                    tv.setBackgroundResource(R.drawable.border_red);
                }
            });
        }

        return newRow;
    }

    @Override
    public void onClick(View v) {

    }

}
