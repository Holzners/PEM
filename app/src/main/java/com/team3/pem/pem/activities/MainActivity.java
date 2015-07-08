package com.team3.pem.pem.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
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

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    SwitchFragment switchFragment;
    FeedReaderDBHelper mDbHelper;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    String titles[];
    int tabNumber = 3;
    private static final String TAG_NEW_EVENT = "newEvent";
    private static final String TAG_NEW_FACTOR = "newFactor";
    Switch selectedSwitch;

    public HashMap<String, Integer> selectedColor;
    DateTime date;
    Menu mMenu;
    boolean contextMenuOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedReaderDBHelper.appContext = this;
        mDbHelper = FeedReaderDBHelper.getInstance();
        setContentView(R.layout.activity_main);
        date = DateTime.today(TimeZone.getDefault());
        selectedColor = new HashMap<>();

        ImageView imageview = new ImageView(this); // Create an icon
        imageview.setImageResource(R.drawable.ic_action_new);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.button_action_accent_selector)
                .setContentView(imageview)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView iconNewFactor = new ImageView(this);
        iconNewFactor.setImageResource(R.drawable.ic_action_new_label);

        ImageView iconNewEvent = new ImageView(this);
        iconNewEvent.setImageResource(R.drawable.ic_action_new_event);


        SubActionButton buttonNewEvent = itemBuilder.setContentView(iconNewEvent).build();
        SubActionButton buttonNewFactor = itemBuilder.setContentView(iconNewFactor).build();
        buttonNewEvent.setOnClickListener(this);
        buttonNewFactor.setOnClickListener(this);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.contentPanel);
        mainLayout.setOnClickListener(this);

        buttonNewEvent.setTag(TAG_NEW_EVENT);
        buttonNewFactor.setTag(TAG_NEW_FACTOR);
        buttonNewEvent.setX(200);


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonNewFactor)
                .addSubActionView(buttonNewEvent)
                .attachTo(actionButton)
//                .setRadius(150)
                .setStartAngle(252)
                .setEndAngle(288)
                .build();
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
                    .setIcon(R.drawable.alert)
                    .show();
            setContextMenuOn(false, null);
            invalidateOptionsMenu();
        } else if (id == R.id.action_edit) {
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

        if (mDbHelper == null)
            mDbHelper = FeedReaderDBHelper.getInstance();

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
        pager.setCurrentItem(1);

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
            mDbHelper.getReadableDatabase().close();
            mDbHelper.close();
        } catch (Exception e) {
            Log.e("Something went wrong", "While closing database");
        }
    }

    public void switchSymptom(boolean isEnabled, String symptom) {
        mDbHelper.switchFactor(symptom, isEnabled);
        refreshAdapters();
    }


    private void updateWeatherData() {
        final Handler handler = new Handler();

        new Thread() {
            public void run() {
                final JSONObject json = RemoteWeatherFetcher.getJSON(MainActivity.this);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.weatherFail),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            String s = WeatherJSONRenderer.renderWeather(json);
                            DateTime today = DateTime.today(TimeZone.getDefault());
                            mDbHelper.saveWeatherDay(today, s);
                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.last_weather_update_key), today + "");
                            editor.apply();
                        }
                    });
                }
            }
        }.start();
    }

    public void goToMonth(int month, int year, String factor) {

        if (!factor.equals("")) {
            for (Map.Entry<String, Boolean> e : mDbHelper.getFactorEnabledMap().entrySet()) {
                if (e.getKey().equals(factor)) e.setValue(true);
                else e.setValue(false);
            }
        }
        adapter.goToMonth(month, year);
        pager.setCurrentItem(1);
    }

    private void updateSelectedColors() {
        for (String s : mDbHelper.getFactorList()) {
            if (!selectedColor.containsKey(s)) selectedColor.put(s, 1);
        }
    }

    public void saveDay(DateTime date, String description) {
        this.mDbHelper.saveDay(date, selectedColor, description);
        refreshAdapters();
    }

    public void showRateDay(View view) {
        DateTime today = DateTime.today(TimeZone.getDefault());
        showRateDayPopup(today);
    }

    public void showRateDayPopup(DateTime today) {
        RateDayFragment rateDayFragment = RateDayFragment.getInstance(today, this);
        FragmentManager f = getSupportFragmentManager();
        rateDayFragment.show(f, "TAG");
    }

    public void showNewFactorDialog() {
        NewFactorFragment newFactorFragment = NewFactorFragment.getInstance(this, null, null);
        FragmentManager f = getSupportFragmentManager();
        newFactorFragment.show(f, "TAG");
    }
    public void showDetailDay(DateTime date) {
        DayDetailFragment dayDetailFragment = DayDetailFragment.newInstance(date);
        FragmentManager f = getSupportFragmentManager();
        dayDetailFragment.show(f, "TAG");
    }

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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.contentPanel){
            if(this.contextMenuOn) {
                setContextMenuOn(false, null);
                invalidateOptionsMenu();
            }
        }else {
            if (view.getTag().equals(TAG_NEW_EVENT)) {
                showRateDay(view);
            }

            if (view.getTag().equals(TAG_NEW_FACTOR)) {
                showNewFactorDialog();
            }
        }
    }
}
