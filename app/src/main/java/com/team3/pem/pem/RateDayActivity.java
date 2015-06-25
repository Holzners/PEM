package com.team3.pem.pem;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.view.adapters.RateDayAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class RateDayActivity extends ActionBarActivity {

    FeedReaderDBHelper mDBHelper;
    public HashMap<String, Integer> selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDBHelper = FeedReaderDBHelper.getInstance();
        setContentView(R.layout.activity_rate_day);
        Button btnSave = (Button)findViewById(R.id.saveButton);
        ListView listView = (ListView) findViewById(R.id.listViewRatings);

        HashMap<String,String> factorAsString = mDBHelper.getFactorsFromDatabase();

        RateDayAdapter adapter = new RateDayAdapter(this, R.layout.rate_day_layout, factorAsString);
        listView.setAdapter(adapter);

        final EditText editText = (EditText) findViewById(R.id.noteText);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Date date = new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                mDBHelper.saveDay(date, selectedColor, editText.getText().toString());
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
