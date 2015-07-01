package com.team3.pem.pem.activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.RateDayAdapter;
import com.team3.pem.pem.adapters.ViewPagerAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.openWeatherApi.RemoteWeatherFetcher;
import com.team3.pem.pem.openWeatherApi.WeatherJSONRenderer;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.view.CalendarFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.SwitchFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

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

    static final int RATE_DAY_DIALOG = 0;
    public HashMap<String, Integer> selectedColor;
    ListView dialogListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedReaderDBHelper.appContext = this;
        mDHelber = FeedReaderDBHelper.getInstance();
        factorAsString = new HashMap<>();
        factorsEnabledMap = new HashMap<>();
        setContentView(R.layout.activity_main);

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
            showDialog(RATE_DAY_DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDHelber == null) mDHelber.getInstance();
        factorAsString = mDHelber.getFactorsFromDatabase();

        for (Map.Entry<String, String> e : factorAsString.entrySet()) {
            if (!factorsEnabledMap.containsKey(e.getKey())) factorsEnabledMap.put(e.getKey(), true);
        }
        // Creating The Toolbar and setting it as the Toolbar for the activity
        initSwitchFragment();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        titles = getResources().getStringArray(R.array.tabs);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, tabNumber, factorAsString);

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
        if(dialogListView!= null) ((RateDayAdapter)dialogListView.getAdapter()).setFactorColors(factorAsString);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        final Dialog dialog = new Dialog(MainActivity.this);
        if(id == RATE_DAY_DIALOG) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.rate_day_dialog);

            dialogListView = (ListView) dialog.findViewById(R.id.rateDayList);
            HashMap<String, String> factorsFromDatabase = mDHelber.getFactorsFromDatabase();

            RateDayAdapter rateDayAdapteradapter = new RateDayAdapter(this, R.layout.rate_day_layout, factorsFromDatabase);
            dialogListView.setAdapter(rateDayAdapteradapter);

            ImageView newFactor = (ImageView) dialog.findViewById(R.id.newFactor);

            newFactor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NewFactorActivity.class));
                }
            });

            Button saveDay = (Button) dialog.findViewById(R.id.saveDay);
            final EditText editText = (EditText) dialog.findViewById(R.id.editNote);
            List<String> factors = mDHelber.getFactors();
            DateTime date = DateTime.today(TimeZone.getDefault());
            DayEntry entry = mDHelber.getDatabaseEntriesDay(factors, date.getDay(), date.getMonth(), date.getYear());
            if(entry != null)
                editText.setText(entry.description);

            saveDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTime date = DateTime.today(TimeZone.getDefault());
                    mDHelber.saveDay(date, selectedColor, editText.getText().toString());
                    adapter.notifyFragment();
                    dialog.dismiss();
                }
            });
        }
        return dialog;
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
        factorsEnabledMap.put(symptom, isEnabled);
        adapter.notifyFragment();
        if (isEnabled) {
            updateSymptoms();
        } else {
            updateSymptoms();
        }
    }
    public HashMap<String, Boolean> getFactorsEnabledMap(){
        return factorsEnabledMap;
    }

    private void updateWeatherData(){
        final Handler handler = new Handler();

        new Thread(){
            public void run(){
                final JSONObject json = RemoteWeatherFetcher.getJSON(MainActivity.this);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(MainActivity.this,"output empty",
                                     Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            String s = WeatherJSONRenderer.renderWeather(json);
                            DateTime today = DateTime.today(TimeZone.getDefault());
                            mDHelber.saveWeatherDay(today ,s);
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
            for(Map.Entry<String, Boolean> e : factorsEnabledMap.entrySet()){
                if(e.getKey().equals(factor)) e.setValue(true);
                else e.setValue(false);
            }
        }
        adapter.goToMonth(month , year);
        pager.setCurrentItem(1);
    }

}
