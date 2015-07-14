package com.team3.pem.pem.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.ViewPagerAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.openWeatherApi.RemoteWeatherFetcher;
import com.team3.pem.pem.openWeatherApi.WeatherJSONRenderer;
import com.team3.pem.pem.view.DayDetailFragment;
import com.team3.pem.pem.view.NewFactorFragment;
import com.team3.pem.pem.view.RateDayFragment;
import com.team3.pem.pem.view.SlidingTabLayout;
import com.team3.pem.pem.view.SwitchFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class MainActivity extends ActionBarActivity {

    private SwitchFragment switchFragment;
    private FeedReaderDBHelper mDbHelper;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private Switch selectedSwitch;

    private HashMap<String, Integer> selectedColor;
    private DateTime date;
    private Menu mMenu;
    private boolean contextMenuOn = false;

    private  static final int TABNUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init Database Helper Singleton
        FeedReaderDBHelper.appContext = this;
        mDbHelper = FeedReaderDBHelper.getInstance();
        //Set View
        setContentView(R.layout.activity_main);
        //Get todays date
        date = DateTime.today(TimeZone.getDefault());

        //Init FAB
        ImageView imageview = new ImageView(this); // Create an icon
        imageview.setImageResource(R.drawable.plus);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.button_action_accent_selector)
                .setContentView(imageview)
                .build();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateDayPopup(date);
            }
        });

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.contentPanel);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contextMenuOn) {
                    selectedSwitch.setTextColor(Color.BLACK);
                    setContextMenuOn(false, null);
                    invalidateOptionsMenu();
                }
            }
        });

        initSelectedColor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Save the menu reference
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export) {
            startActivity(new Intent(MainActivity.this, ExportActivity.class));
        } else if (id == R.id.action_reminder) {
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
        } else if (id == R.id.action_delete) {
            selectedSwitch.setTextColor(Color.BLACK);
            final String switchName = selectedSwitch.getText().toString();
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.deleteSymptom))
                    .setMessage(String.format(getResources().getString(R.string.deleteAlert), switchName))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mDbHelper.deleteFactor(switchName, mDbHelper.getFactorList());
                    refreshAdapters();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_dialog_alert)
                    .show();
            setContextMenuOn(false, null);
            invalidateOptionsMenu();
        } else if (id == R.id.action_edit) {
            selectedSwitch.setTextColor(Color.BLACK);
            String color = mDbHelper.getFactorColorMap().get(selectedSwitch.getText().toString());
            NewFactorFragment newFactorFragment = NewFactorFragment.getInstance(this, selectedSwitch.getText().toString(), color);
            FragmentManager f = getSupportFragmentManager();
            newFactorFragment.show(f, "TAG");
            setContextMenuOn(false, null);
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Check if DB Helper is null
        if (mDbHelper == null)
            mDbHelper = FeedReaderDBHelper.getInstance();

        //Init Button Fragment
        initSwitchFragment();
        // Creating The Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        String[] titles = getResources().getStringArray(R.array.tabs);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, TABNUMBER);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);

        // Assiging the Sliding Tab Layout View
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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

        //Check if todays Weather is fetched
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String lastUpdate = sharedPref.getString(getString(R.string.last_weather_update_key), "");
        String today = date + "";
        if (lastUpdate != null && !lastUpdate.equalsIgnoreCase(today)) {
            //If not get Weather data
            updateWeatherData();
        }
        //get Selected Colors
        updateSelectedColors();

        if (this.getIntent().getBooleanExtra("openDialog", false)) {
            this.getIntent().removeExtra("openDialog");
            showRateDayPopup(date);
        }
    }

    /**
     * Init Switch Button List Fragment and add it to Content View
     */
    public void initSwitchFragment() {
        this.switchFragment = new SwitchFragment();
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.switchFragmentPanel, switchFragment);
        t.commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            //Close database and Helper
            mDbHelper.getReadableDatabase().close();
            mDbHelper.close();
        } catch (Exception e) {
            Log.e("Something went wrong", "While closing database");
        }
    }

    /**
     * Enabled Mode of "symptom" switched to "isEnabled"
     * @param isEnabled
     * @param symptom
     */
    public void switchSymptom(boolean isEnabled, String symptom) {
        //Notify Database
        mDbHelper.switchFactor(symptom, isEnabled);
        //Notify Calendar Views
        refreshAdapters();
    }

    /**
     * Fetch weather data in Background
     */
    private void updateWeatherData() {
        final Handler handler = new Handler();

        new Thread() {
            public void run() {
                //send Request to API und get JSON Response
                final JSONObject json = RemoteWeatherFetcher.getJSON(MainActivity.this);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            //Fetching failed make toast
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.weatherFail),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            //Convert JSON Response to String and Store in DataBase for today
                            String s = WeatherJSONRenderer.renderWeather(json);
                            mDbHelper.saveWeatherDay(date, s);
                            //Set Shared Preferences todays Weather Fetched
                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.last_weather_update_key), date + "");
                            editor.apply();
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * Show Month View with only one Factor selected
     * --> Year View onCLick in single symptom Month
     * If factor == "" go to Month dont change Switch states
     * @param month
     * @param year
     * @param factor
     */
    public void goToMonth(int month, int year, String factor) {

        if (!factor.equals("")) {
            //Disable all other Symtpoms
            for (Map.Entry<String, Boolean> e : mDbHelper.getFactorEnabledMap().entrySet()) {
                if (e.getKey().equals(factor)) e.setValue(true);
                else e.setValue(false);
            }
        }
        //Page to Month View
        adapter.goToMonth(month, year);
        pager.setCurrentItem(1);
    }

    /**
     * Updates Selected Colors for Rate Day
     */
    private void updateSelectedColors() {
        for (String s : mDbHelper.getFactorList()) {
            if (!selectedColor.containsKey(s)) selectedColor.put(s, 1);
        }
    }

    /**
     * Save selected Ratings in Rate Day view
     * @param date
     * @param description
     */
    public void saveDay(DateTime date, String description) {
        //save in DB
        this.mDbHelper.saveDay(date, selectedColor, description);
        //Notify view
        refreshAdapters();
    }

    /**
     * Show Rate Day Dialog Fragment for any day
     * @param today
     */
    public void showRateDayPopup(DateTime today) {
        RateDayFragment rateDayFragment = RateDayFragment.getInstance(today, this);
        FragmentManager f = getSupportFragmentManager();
        rateDayFragment.show(f, "TAG");
    }

    /**
     * Show Dialog Fragment for creating new Factors/Symptoms
     * Called by row_switch_layout.xml
     */
    public void showNewFactor(View view) {
        NewFactorFragment newFactorFragment = NewFactorFragment.getInstance(this, null, null);
        FragmentManager f = getSupportFragmentManager();
        newFactorFragment.show(f, "TAG");
    }

    /**
     * Show Details for selected date in DialogFragment
     * @param date
     */
    public void showDetailDay(DateTime date) {
        DayDetailFragment dayDetailFragment = DayDetailFragment.newInstance(date);
        FragmentManager f = getSupportFragmentManager();
        dayDetailFragment.show(f, "TAG");
    }

    /**
     * Notify ListFragments and Calendar Views for data have change
     */
    public void refreshAdapters() {
        adapter.notifyFragment();
        switchFragment.notifyAdapter();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (contextMenuOn) {
            getmMenu().clear();
            getMenuInflater().inflate(R.menu.menu_context, getmMenu());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void setContextMenuOn(boolean contextMenuOn, Switch selectedSwitch) {
        this.contextMenuOn = contextMenuOn;
        this.selectedSwitch = selectedSwitch;
    }

    public Menu getmMenu() {
        return mMenu;
    }

    /**
     * Get the current selected colors Hashmap
     *
     * @return
     */
    public HashMap<String, Integer> getSelectedColor(){
        return this.selectedColor;
    }

    /**
     * Init selected colors Hashmap
     */
    public void initSelectedColor(){
        this.selectedColor = new HashMap<>();
    }
}
