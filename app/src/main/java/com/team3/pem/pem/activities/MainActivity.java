package com.team3.pem.pem.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.ViewPagerAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.openWeatherApi.RemoteWeatherFetcher;
import com.team3.pem.pem.openWeatherApi.WeatherJSONRenderer;
import com.team3.pem.pem.view.DayDetailFragment;
import com.team3.pem.pem.view.NewFactorFragment;
import com.team3.pem.pem.view.RateDayFragment;
import com.team3.pem.pem.view.RemoveFactorFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.SwitchFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MainActivity extends ActionBarActivity{

    SwitchFragment switchFragment;
    FeedReaderDBHelper mDbHelper;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    String titles[];

    int tabNumber =3;

    static final int RATE_DAY_DIALOG = 0;
    public HashMap<String, Integer> selectedColor;

    DateTime date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FeedReaderDBHelper.appContext = this;
        mDbHelper = FeedReaderDBHelper.getInstance();
        setContentView(R.layout.activity_main);
        date = DateTime.today(TimeZone.getDefault());
        selectedColor = new HashMap<>();
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
        }else if(id == R.id.action_reminder){
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
        }else if(id == R.id.newFactorRating || id == R.id.action_rateDay){
            showRateDayPopup(DateTime.today(TimeZone.getDefault()));
        }
        else if (id == R.id.action_addFactor) {
            showNewFactorDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDbHelper == null) mDbHelper.getInstance();

        // Creating The Toolbar and setting it as the Toolbar for the activity
        initSwitchFragment();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        titles = getResources().getStringArray(R.array.tabs);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, tabNumber);

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


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String lastUpdate = sharedPref.getString(getString(R.string.last_weather_update_key), "");
        String today = DateTime.today(TimeZone.getDefault()) + "";
        if (!lastUpdate.equalsIgnoreCase(today)) {
            updateWeatherData();
        }
       // if(pemDialogFragment != null) ((RateDayAdapter)pemDialogFragment.getListAdapter()).notifyDataSetChanged();
        updateSelectedColors();
//        Log.d("Heutiges Wetter", mDbHelper.getWeatherData(DateTime.today(TimeZone.getDefault())));
    }


    public void initSwitchFragment(){
        this.switchFragment = new SwitchFragment();
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.switchFragmentPanel, switchFragment);
        t.commit();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            mDbHelper.getReadableDatabase().close();
            mDbHelper.close();
        }catch (Exception e){
            Log.e("Something went wrong", "While closing database");
        }
    }

    public void switchSymptom(boolean isEnabled, String symptom){
        mDbHelper.getFactorEnabledMap().put(symptom, isEnabled);
        refreshAdapters();

    }


    private void updateWeatherData(){
        final Handler handler = new Handler();

        new Thread(){
            public void run(){
                final JSONObject json = RemoteWeatherFetcher.getJSON(MainActivity.this);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(MainActivity.this,getResources().getString(R.string.weatherFail),
                                     Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            String s = WeatherJSONRenderer.renderWeather(json);
                            DateTime today = DateTime.today(TimeZone.getDefault());
                            mDbHelper.saveWeatherDay(today, s);
                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.last_weather_update_key), today+"");
                            editor.commit();
                        }
                    });
                }
            }
        }.start();
    }

    public void goToMonth(int month , int year, String factor){

        if(!factor.equals("")){
            for(Map.Entry<String, Boolean> e : mDbHelper.getFactorEnabledMap().entrySet()){
                if(e.getKey().equals(factor)) e.setValue(true);
                else e.setValue(false);
            }
        }
        adapter.goToMonth(month, year);
        pager.setCurrentItem(1);
    }
    private void updateSelectedColors(){
        for(String s : mDbHelper.getFactorList()){
            if(!selectedColor.containsKey(s)) selectedColor.put(s,1);
        }
    }

    public void saveDay(DateTime date , String description) {
        this.mDbHelper.saveDay(date, selectedColor, description);
        refreshAdapters();
    }

    public void showRateDayPopup(DateTime today) {
        this.date = today;
        RateDayFragment rateDayFragment = RateDayFragment.getInstance(today, this);
        FragmentManager f = getSupportFragmentManager();
        rateDayFragment.show(f, "TAG");
    }

    public void showNewFactorDialog(){
        NewFactorFragment newFactorFragment = NewFactorFragment.getInstance(this);
        FragmentManager f = getSupportFragmentManager();
        newFactorFragment.show(f, "TAG");
    }

    public void showRemoveFactorDialog(){
        RemoveFactorFragment removeFactorFragment = RemoveFactorFragment.getInstance(this);
        FragmentManager f = getSupportFragmentManager();
        removeFactorFragment.show(f, "TAG");
    }

    public void showDetailDay(DateTime date){
        DayDetailFragment dayDetailFragment = DayDetailFragment.newInstance(date);
        FragmentManager f = getSupportFragmentManager();
        dayDetailFragment.show(f, "TAG");
    }

    public void refreshAdapters(){
        adapter.notifyFragment();
    }
}
