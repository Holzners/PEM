package utili;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephan on 18.06.15.
 */
public interface IDatabaseHelper {

    /**
     * Saves new Factor in Database
     * @param factorName
     * @param colorId
     */
    void saveFactor(String factorName, int colorId);

    /**
     * saves Users rate of specific day
     * @param date - of rated Day
     * @param ratings - List of Ratings
     * @param text - Description
     */
    void saveDay(Date date, List<Integer> ratings, String text);

    /**
     * Returns each entry to a specific List of factors
     * @param factors - list of factors
     * @return HashMap<Date, HashMap<String,List<Integer>>>
     */
    HashMap<Date, HashMap<String,List<Integer>>> getDatabaseEntries(List<String> factors);

    /**
     * Returns  entries for Date(day.month.year) to a specific List of factors
     * @param factors
     * @param day
     * @param month
     * @param year
     * @return HashMap<Date, HashMap<String,List<Integer>>>
     */
    HashMap<Date, HashMap<String,List<Integer>>> getDatabaseEntriesDay(List<String> factors, int day, int month, int year);
    /**
     * Returns  entries for Month(month.year) to a specific List of factors
     * @param factors
     * @param month
     * @param year
     * @return HashMap<Date, HashMap<String,List<Integer>>>
     */
    HashMap<Date, HashMap<String,List<Integer>>> getDatabaseEntriesMonth(List<String> factors, int month, int year);
    /**
     * Returns  entries for Date(day.month.year) to a specific List of factors
     * @param factors
     * @param year
     * @return HashMap<Date, HashMap<String,List<Integer>>>
     */
    HashMap<Date, HashMap<String,List<Integer>>> getDatabaseEntriesYear(List<String> factors, int year);

    /**
     * Returns all saved Factors with Color as Resource ID
     * @return  HashMap<String, Integer>
     */
    HashMap<String, Integer> getFactorsFromDatabase();

    /**
     * Returns all saved Factors
     * @return
     */
    List<String> getFactors ();
}