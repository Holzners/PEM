package com.team3.pem.pem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.roomorama.caldroid.CaldroidFragment;
import com.team3.pem.pem.view.CalendarFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    HashMap<Date, Integer> daysToModify;

    CalendarFragment caldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daysToModify = new HashMap<>();

        initMonthFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }else if(id == R.id.action_export){
            startActivity(new Intent(MainActivity.this, ExportActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMonthFragment(){
        this.caldroidFragment = new CalendarFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.refreshView();
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.contentPanel, caldroidFragment);
        t.commit();
    }

    private void modifyDays(){

        chooseDaysToModify();

        for(Map.Entry<Date, Integer> e : daysToModify.entrySet()){

            Log.d(e.getKey().toString(), e.getValue() + "");

            caldroidFragment.clearBackgroundResourceForDate(e.getKey());
            caldroidFragment.setBackgroundResourceForDate(e.getValue(), e.getKey());
        }
        caldroidFragment.refreshView();
    }

    private void chooseDaysToModify(){

        Date date = new Date(2015, 06 , 12);
        Date date1 = new Date(2015, 06 , 11);
        Date date2 = new Date(2015, 06 , 10);
        Date date3 = new Date(2015, 06 , 9);
        Date date4 = new Date(2015, 06 , 8);

        daysToModify.put(date, R.color.blue);
        daysToModify.put(date1, R.color.black);
        daysToModify.put(date2, R.color.green);
        daysToModify.put(date3, R.color.darkgreen);
        daysToModify.put(date4, R.color.red);

    }

}
