package com.team3.pem.pem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.roomorama.caldroid.CaldroidFragment;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.SQLiteMethods;
import com.team3.pem.pem.view.CalendarFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.SwitchFragment;
import com.team3.pem.pem.view.ViewPagerAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//import static com.team3.pem.pem.R.id.calendarFragmentPanel;


public class MainActivity extends ActionBarActivity {

    CalendarFragment caldroidFragment;

    HashMap<String, Integer> factorAsString;

    FeedReaderDBHelper mDHelber;
    SwitchFragment switchFragment;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Week","Month","Year"};
    int Numboftabs =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedReaderDBHelper.appContext = this;
        mDHelber = FeedReaderDBHelper.getInstance();
        factorAsString = new HashMap<>();
        factorAsString = mDHelber.getFactorsFromDatabase();
        setContentView(R.layout.activity_main);

        initMonthFragment();

        for (Map.Entry<String, Integer> e : factorAsString.entrySet()){
            Log.d(e.getKey() , e.getValue() + "");
        }

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
//        t.add(calendarFragmentPanel, caldroidFragment);
        t.commit();
    }

    public void getDatabaseEntry(){
        SQLiteDatabase dbRwad = mDHelber.getReadableDatabase();
        //String[]
    }

    private HashMap<String, Integer> getFactorsFromDatabase(){
        SQLiteDatabase dbRwad = mDHelber.getReadableDatabase();
        String [] projection = {
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS,
                SQLiteMethods.COLUMN_NAME_ENTRY_COLOR,
        };
        String selection = " * ";

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        HashMap<String , Integer> factors = new HashMap<>();
        while (!cursor.isAfterLast()){
            factors.put(cursor.getString(0), cursor.getInt(1));
            cursor.moveToNext();
        }
        return factors;
    }

}
