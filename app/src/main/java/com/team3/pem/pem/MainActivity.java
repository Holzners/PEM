package com.team3.pem.pem;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.roomorama.caldroid.CaldroidFragment;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.view.CalendarFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    HashMap<Date, Integer> daysToModify;

    CalendarFragment caldroidFragment;

    HashMap<String, Integer> factorAsString;

    public FeedReaderDBHelper mDHelber;
   // SwitchFragment switchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDHelber = new FeedReaderDBHelper(this);
        factorAsString = new HashMap<>();
        factorAsString = mDHelber.getFactorsFromDatabase();
        setContentView(R.layout.activity_main);
        daysToModify = new HashMap<>();
        initMonthFragment();
        initSwitchFragment();

        for (Map.Entry<String, Integer> e : factorAsString.entrySet()){
            Log.d(e.getKey() , e.getValue() + "");
        }

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



// ------------------- SwitchFragment   ------------------------------

    protected void initSwitchFragment(){
 //       this.switchFragment = new SwitchFragment();
        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.contentPanel, switchFragment);
//        ft.commit();
    }

}
