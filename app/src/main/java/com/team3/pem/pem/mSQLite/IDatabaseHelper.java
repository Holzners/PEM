package com.team3.pem.pem.mSQLite;

import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.ReminderModel;

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
    void saveFactor(String factorName, String colorId);

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
     * @param year
     * @return
     */
    HashMap<DateTime, DayEntry> getDatabaseEntriesYear(List<String> factors, int year);

    HashMap<DateTime, DayEntry> getDatabaseEntriesWeek(List<String> factors, int startDay, int month, int year, int endDay, int endMonth, int endYear);

    /**
     * Returns all saved Factors with Color as Resource ID
     * @return  HashMap<String, Integer>
     */
    HashMap<String, String> getFactorsFromDatabase();

    /**
     * Returns all saved Factors
     * @return
     */
    List<String> getFactors ();

    /**
     * Saves new Reminder in Database
     * @param reminder
     */
    void saveReminder(ReminderModel reminder);

    /**
     * removes Reminder from Database
     * @param iD
     */
    void removeReminder(int iD);

    /**
     * return all saved Reminders in Database
     * @return
     */
    List<ReminderModel> getAllReminders();

    /**
     * Returns weatherdata of day
     * @param dateTime
     * @return
     */
    String getWeatherData(DateTime dateTime);

    void deleteFactor(String factor, List<String> factorList);


}
