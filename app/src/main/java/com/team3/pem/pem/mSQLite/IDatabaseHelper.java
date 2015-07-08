package com.team3.pem.pem.mSQLite;

import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.NotificationModel;

import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 18.06.15.
 */
public interface IDatabaseHelper {

    /**
     * Saves new Factor in Database
     * @param factorName
     * @param colorId
     */
    void saveFactor(String factorName, String colorId, boolean isGradual);

    /**
     * saves Users rate of specific day
     * @param date - of rated Day
     * @param ratings - List of Ratings
     * @param text - Description
     */
    void saveDay(DateTime date, HashMap<String, Integer> ratings, String text);

    /**
     *
     * @param date
     * @param weatherData
     */
    void saveWeatherDay(DateTime date, String weatherData);
    /**
     * Returns each entry to a specific List of factors
     * @param factors - list of factors
     * @return
     */
    HashMap<DateTime, DayEntry> getDatabaseEntries(List<String> factors);

    /**
     * Returns  entries for Date(day.month.year) to a specific List of factors
     * @param factors
     * @param day
     * @param month
     * @param year
     * @return
     */
    DayEntry getDatabaseEntriesDay(List<String> factors, int day, int month, int year);
    /**
     * Returns  entries for Month(month.year) to a specific List of factors
     * @param factors
     * @param month
     * @param year
     * @return
     */
    HashMap<DateTime, DayEntry> getDatabaseEntriesMonth(List<String> factors, int month, int year);

    /**
     * Returns  entries for Date(day.month.year) to a specific List of factors
     * @param factors
     * @param startDay
     * @param month
     * @param year
     * @param endDay
     * @param endMonth
     * @param endYear
     * @return
     */
    HashMap<DateTime, DayEntry> getDatabaseEntriesWeek(List<String> factors, int startDay, int month, int year, int endDay, int endMonth, int endYear);



    /**
     * Saves new Reminder in Database
     * @param reminder
     */
    void saveReminder(NotificationModel reminder);

    /**
     * removes Reminder from Database
     * @param iD
     */
    void removeReminder(int iD);

    /**
     * return all saved Reminders in Database
     * @return
     */
    List<NotificationModel> getAllReminders();

    /**
     * Returns weatherdata of day
     * @param dateTime
     * @return
     */
    String getWeatherData(DateTime dateTime);

    /**
     * Delete factor from database
     * @param factor
     * @param factorList
     */
    void deleteFactor(String factor, List<String> factorList);

    /**
     *
     * @param factor
     * @param enabled
     */
    void switchFactor(String factor, boolean enabled);

    /**
     * Returns all saved Factors
     * @return
     */
    List<String> getFactorList();

    /**
     * Returns all saved Factors with Color as Resource ID
     * @return  HashMap<String, String>
     */
    HashMap<String, String> getFactorColorMap();

    /**
     * All factors and their stored mode
     * @return
     */
    HashMap<String, Boolean> getFactorEnabledMap();

    /**
     * All factors and their Types
     * @return
     */
    HashMap<String , Boolean> getFactorIsGradualMap();
}
