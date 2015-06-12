package com.team3.pem.pem.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.team3.pem.pem.R;

import java.util.HashMap;

/**
 * Created by Stephan on 12.06.15.
 */
public class CalendarFragmentAdapter extends CaldroidGridAdapter{

    public CalendarFragmentAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
            if (convertView == null) {
                cellView = inflater.inflate(R.layout.cell_view_layout, null);
            }
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView imgView1 = (TextView) cellView.findViewById(R.id.textView);
        TextView imgView2 = (TextView) cellView.findViewById(R.id.textView2);
        TextView imgView3 = (TextView) cellView.findViewById(R.id.textView3);
        TextView imgView4 = (TextView) cellView.findViewById(R.id.textView4);

        imgView1.setBackgroundColor(cellView.getResources().getColor(R.color.blue));
        imgView2.setBackgroundColor(cellView.getResources().getColor(R.color.red));
        imgView3.setBackgroundColor(cellView.getResources().getColor(R.color.green));
        imgView4.setBackgroundColor(cellView.getResources().getColor(R.color.transparent));


        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        return cellView;
    }
}
