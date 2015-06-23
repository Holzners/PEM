package com.team3.pem.pem;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import com.roomorama.caldroid.CaldroidFragment;
import com.team3.pem.pem.view.CalendarFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.ViewPagerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    HashMap<Date, Integer> daysToModify;

    CalendarFragment caldroidFragment;
   // SwitchFragment switchFragment;
    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Week","Month","Year"};
    int Numboftabs =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        daysToModify = new HashMap<>();

        initMonthFragment();
        initSwitchFragment();
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
        t.add(R.id.calender, caldroidFragment);
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
