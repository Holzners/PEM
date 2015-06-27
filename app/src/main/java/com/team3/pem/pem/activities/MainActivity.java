package com.team3.pem.pem.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.roomorama.caldroid.CaldroidFragment;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.ViewPagerAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ReminderModel;
import com.team3.pem.pem.view.CalendarFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.SwitchFragment;
import com.team3.pem.pem.view.WeekFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.team3.pem.pem.R.id.calendarFragmentPanel;


public class MainActivity extends ActionBarActivity implements SwitchFragment.SwitchFragmentInterface {

    CalendarFragment caldroidFragment;
    SwitchFragment switchFragment;
    HashMap<String, String> factorAsString;
    HashMap<String, Boolean> factorsEnabledMap;
    FeedReaderDBHelper mDHelber;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    String titles[];

    int tabNumber =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedReaderDBHelper.appContext = this;
        mDHelber = FeedReaderDBHelper.getInstance();
        factorAsString = new HashMap<>();
        factorsEnabledMap = new HashMap<>();
        setContentView(R.layout.activity_main);
        //checkDatabase();
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
        if(id == R.id.action_export){
            startActivity(new Intent(MainActivity.this, ExportActivity.class));
        }else if(id == R.id.action_notifications){
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
        }else if(id == R.id.action_rateday){
            openPopUpForDayRating();
        } else if (id == R.id.action_new_factor){
            startActivity(new Intent(MainActivity.this, NewFactorActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mDHelber == null) mDHelber.getInstance();
        factorAsString = mDHelber.getFactorsFromDatabase();

        for(Map.Entry<String, String> e : factorAsString.entrySet()){
            factorsEnabledMap.put(e.getKey() , true);
        }
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        titles = getResources().getStringArray(R.array.tabs);
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles, tabNumber,factorAsString);

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
        initSwitchFragment();
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
        t.replace(R.id.calender, caldroidFragment);
        t.commit();
    }


    private void initWeekFragment(){
        WeekFragment weekFragment = new WeekFragment();
        weekFragment.init();
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calender, weekFragment);
        t.commit();
    }


//---------------------SwitchFragment---------------------------------

    public void initSwitchFragment(){
        this.switchFragment = new SwitchFragment();
        FragmentManager f = getFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.switchFragmentPanel, switchFragment);
        t.commit();
    }

    @Override
    public void updateSymptoms() {
        // TODO Farbe/Symptom (de)aktivieren
    }

    public void onSwitchClicked(View view){
        boolean on = ((Switch) view).isChecked();
        // TODO Switch Position
        if (on) {
            Log.i("onSwitchClicked","Switch isChecked");
            updateSymptoms();
        } else {
            Log.i("onSwitchClicked","Switch isNotChecked");
            updateSymptoms();
        }
    }

    private void openPopUpForDayRating() {
        startActivity(new Intent(MainActivity.this, RateDayActivity.class));
    }

    private void checkDatabase(){
        List<ReminderModel> reminders = mDHelber.getAllReminders();
        for(ReminderModel r : reminders) {
            Log.d("Reminder ID", r.getAlarmID()+"");
            Log.d("Dialog ID", r.getDialogID()+"");
            Log.d("Time", r.getTime()+"");
            Log.d("Text", r.getText()+"");
            Log.d("Active", r.isActive()+"");
            for(Boolean b : r.getActiveForDays()){
                Log.d("Boolen" , b + "");
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            mDHelber.getReadableDatabase().close();
            mDHelber.close();
        }catch (Exception e){
            Log.e("Something went wrong", "While closing database");
        }
    }

    public List<String> getSymptomList(){
        return mDHelber.getFactors();
    }

    public HashMap<String, String> getFactorWithColor(){;
        return factorAsString;
    }

    public void switchSymptom(boolean isEnabled, String symptom){
        factorsEnabledMap.put(symptom,isEnabled);
        adapter.notifyFragment();
    }
    public HashMap<String, Boolean> getFactorsEnabledMap(){
        return factorsEnabledMap;
    }

}