package com.team3.pem.pem.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team3.pem.pem.view.CalendarFragment;
import com.team3.pem.pem.view.WeekFragment;
import com.team3.pem.pem.view.YearFragment;

import hirondelle.date4j.DateTime;

/**
 * Created by Olli
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    CalendarFragment monthfragment;
    WeekFragment weekFragment;
    YearFragment yearFragment;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;



    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
    //TODO
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            weekFragment = new WeekFragment();
            weekFragment.init();
            return weekFragment;
        }
        else if(position ==1)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            monthfragment = new CalendarFragment();
            return monthfragment;
        }else {
            yearFragment = new YearFragment();
            return yearFragment;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void notifyFragment(){
        if(weekFragment != null) weekFragment.notifyAdapter();
        if(monthfragment!= null) monthfragment.notifyAdapter();
        if(yearFragment != null) yearFragment.notifyAdapter();
    }

    public void goToMonth(int month , int year){
        if(monthfragment!= null) {
            DateTime date = DateTime.forDateOnly(year, month, 1);
            monthfragment.moveToDateTime(date);
        }
    }

}