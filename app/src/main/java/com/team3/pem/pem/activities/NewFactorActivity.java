package com.team3.pem.pem.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ColorsToPick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewFactorActivity extends ActionBarActivity {

    EditText newFactorText;
    TextView toManySymptomsText;
    FloatingActionButton FAB;
    Button deleteButton;
    FeedReaderDBHelper mDBHelper;
    Spinner colorSpinner;
    String selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_factor);
        setTitle("Add New Factor");
        mDBHelper = FeedReaderDBHelper.getInstance();
        FAB = (FloatingActionButton) findViewById(R.id.FAB);
        deleteButton = (Button) findViewById(R.id.delete_button);
        newFactorText =(EditText) findViewById(R.id.inputText);
        toManySymptomsText = (TextView) findViewById(R.id.toManySymptomsText);
        toManySymptomsText.setVisibility(View.INVISIBLE);

        HashMap<String,String> factorEntries = mDBHelper.getFactorsFromDatabase();

        if(factorEntries.size() == ColorsToPick.values().length){
            toManySymptomsText.setVisibility(View.VISIBLE);
            FAB.setEnabled(false);
            newFactorText.setEnabled(false);
        }else{
            List<String> colors =new ArrayList<>();
            for(int i = 0 ; i < ColorsToPick.values().length && colors.size()<(ColorsToPick.values().length -factorEntries.size());i++){
                if(!factorEntries.containsValue(ColorsToPick.values()[i].name())){
                    colors.add(ColorsToPick.values()[i].name());
                }else{
                    Log.d("Color vergeben" , ColorsToPick.values()[i].name());
                }
            }


            colorSpinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorSpinner.setAdapter(dataAdapter);

            colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  selectedColor = parent.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFactor();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_factor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void saveFactor(){
        if(!newFactorText.getText().toString().equals("")){
            mDBHelper.saveFactor(newFactorText.getText().toString(), selectedColor);
            finish();
        }else{
            Toast.makeText(this, "No Input!", Toast.LENGTH_LONG).show();
        }
    }



}
