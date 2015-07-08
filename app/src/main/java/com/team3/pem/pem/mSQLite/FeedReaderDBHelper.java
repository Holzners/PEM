package com.team3.pem.pem.mSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.team3.pem.pem.utili.ColorsToPick;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.NotificationModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Stephan on 16.06.15.
 */
public class FeedReaderDBHelper extends SQLiteOpenHelper implements IDatabaseHelper {

    public static Context appContext;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MedicalJournal.db";

    public static FeedReaderDBHelper mdbHelper;

    private List<String> factorList;
    private HashMap<String , String> factorColorMap;
    private HashMap<String , Boolean> factorEnabledMap;

    private FeedReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mdbHelper = this;
    }

    public static FeedReaderDBHelper getInstance() {
        if (mdbHelper != null) return mdbHelper;
        return new FeedReaderDBHelper(appContext);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteMethods.SQL_CREATE_ENTRIES);
        db.execSQL(SQLiteMethods.SQL_CREATE_FACTORS);
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Kopfschmerzen"));
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Bauchschmerzen"));
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Müdigkeit"));
        db.execSQL(SQLiteMethods.SQL_CREATE_REMINDERS);
        db.execSQL(SQLiteMethods.SQL_CREATE_WEATHER);

        DateTime startDate = DateTime.forDateOnly(2014, 1, 1);
        DateTime yesterday = DateTime.today(TimeZone.getDefault());

        while (!startDate.isSameDayAs(yesterday)) {
            ContentValues values = new ContentValues();
            values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, startDate.getDay());
            values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, startDate.getMonth());
            values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, startDate.getYear());
            Random rand = new Random();

            values.put("Kopfschmerzen", rand.nextInt(5) + 1);
            values.put("Bauchschmerzen", rand.nextInt(5) + 1);
            values.put("Müdigkeit", rand.nextInt(5) + 1);
            values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

            db.insertWithOnConflict(
                    SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                    "null", values, SQLiteDatabase.CONFLICT_REPLACE);

            ContentValues values2 = new ContentValues();
            values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, startDate.getDay());
            values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, startDate.getMonth());
            values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, startDate.getYear());

            values2.put("Kopfschmerzen", rand.nextInt(5) + 1);
            values2.put("Bauchschmerzen", rand.nextInt(5) + 1);
            values2.put("Müdigkeit", rand.nextInt(5) + 1);
            values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

            db.insertWithOnConflict(
                    SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                    "null", values2, SQLiteDatabase.CONFLICT_REPLACE);

            startDate = startDate.plusDays(1);

            Log.d("Startday", startDate.toString());
            Log.d("EndDay", yesterday.toString());
        }
        ContentValues values3 = new ContentValues();
        values3.put(SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS, "Kopfschmerzen");
        values3.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, ColorsToPick.BLUE.name());
        values3.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, 1);

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values3, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values4 = new ContentValues();
        values4.put(SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS, "Bauchschmerzen");
        values4.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, ColorsToPick.RED.name());
        values4.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, 0);

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values4, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values5 = new ContentValues();
        values5.put(SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS, "Müdigkeit");
        values5.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, ColorsToPick.RED.name());
        values5.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, 1);

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values5, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values10 = new ContentValues();

        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_REMINDER_SUNDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_MONDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_TUESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_THURSDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_FRIDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_SATURDAY};
        values10.put(SQLiteMethods.COLUMN_NAME_REMINDER_ID, 0);
        values10.put(SQLiteMethods.COLUMN_NAME_REMINDER_DIALOG_ID, 0);
        values10.put(SQLiteMethods.COLUMN_NAME_REMINDER_TEXT, "Tag bewerten");
        values10.put(SQLiteMethods.COLUMN_NAME_REMINDER_TIME, "08:00");
        values10.put(SQLiteMethods.COLUMN_NAME_REMINDER_ACTIVE, 0);

        for (int i = 0; i < dayColumns.length; i++) {
            values10.put(dayColumns[i], 0);
        }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                "null", values10, SQLiteDatabase.CONFLICT_REPLACE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void saveFactor(String factorName, String colorId) {
        this.getFactorList();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS, factorName);
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, colorId);
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_ENABLED, 1);
        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
        if(!factorList.contains(factorName)) {
            db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, factorName));
            this.getFactorList().add(factorName);
        }

        this.getFactorColorMap().put(factorName,colorId);
        this.getFactorEnabledMap().put(factorName,true);

    }

    public void switchFactor(String factor, boolean enabled){
        this.getFactorList();
        this.getFactorColorMap();
        this.getFactorEnabledMap().put(factor, enabled);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = (enabled) ? 1 : 0;
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS, factor);
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_COLOR, factorColorMap.get(factor));
        values.put(SQLiteMethods.COLUMN_NAME_FACTOR_ENABLED, i);
        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    @Override
    public void saveDay(DateTime date, HashMap<String, Integer> ratings, String text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, date.getDay());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, date.getMonth());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, date.getYear());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, text);

        for (Map.Entry<String, Integer> e : ratings.entrySet()) {
            Log.d(e.getKey(), e.getValue() + "");
            values.put(e.getKey(), e.getValue());
        }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);


    }

    @Override
    public void saveWeatherDay(DateTime date, String weatherData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, date.getDay());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, date.getMonth());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, date.getYear());
        values.put(SQLiteMethods.COLUMN_NAME_WEATHER_WEATHER, weatherData);

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_WEATHER_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    @Override
    public HashMap<DateTime, DayEntry> getDatabaseEntries(List<String> factors) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<DateTime, DayEntry> entryMap = new HashMap<>();

        String[] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length - 1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for (int i = 0; i < factors.size(); i++) {
            projection[3 + i] = factors.get(i);
        }

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DateTime date = DateTime.forDateOnly(cursor.getInt(2), cursor.getInt(1), cursor.getInt(0));
            HashMap<String, Integer> colors = new HashMap<>();

            for (int i = 3; i < factors.size() + 3; i++) {
                colors.put(factors.get(i-3), cursor.getInt(i));
            }

            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length - 1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        cursor.close();
        return entryMap;
    }

    @Override
    public DayEntry getDatabaseEntriesDay(List<String> factors, int day, int month, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        String[] projection = null;

        if(factors != null) {
            projection = new String[4 + factors.size()];

            projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
            projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
            projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
            projection[projection.length - 1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

            for (int i = 0; i < factors.size(); i++) {
                projection[3 + i] = factors.get(i);
            }
        }
        String selection =
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY + " = " + day + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + month + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + year;

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        DayEntry descriptionColorMap = null;
        while (!cursor.isAfterLast()) {
            HashMap<String, Integer> colors = new HashMap<>();

            for (int i = 3; i < factors.size() + 3; i++) {
                colors.put(factors.get(i - 3), cursor.getInt(i));
            }
            descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length - 1));
            cursor.moveToNext();

        }
        cursor.close();
        return descriptionColorMap;
    }

    @Override
    public HashMap<DateTime, DayEntry> getDatabaseEntriesMonth(List<String> factors, int month, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<DateTime, DayEntry> entryMap = new HashMap<>();
        String[] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length - 1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for (int i = 0; i < factors.size(); i++) {
            projection[3 + i] = factors.get(i);
        }

        String selection =
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + month + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + year;

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DateTime date = DateTime.forDateOnly(cursor.getInt(2), cursor.getInt(1), cursor.getInt(0));
            HashMap<String, Integer> colors = new HashMap<>();

            for (int i = 3; i < factors.size() + 3; i++) {
                colors.put(factors.get(i - 3), cursor.getInt(i));
            }
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length - 1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        cursor.close();
        return entryMap;
    }

    @Override
    public HashMap<DateTime, DayEntry> getDatabaseEntriesYear(List<String> factors, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<DateTime, DayEntry> entryMap = new HashMap<>();
        String[] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length - 1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for (int i = 0; i < factors.size(); i++) {
            projection[3 + i] = factors.get(i);
        }

        String selection =
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + year;

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DateTime date = DateTime.forDateOnly(cursor.getInt(2), cursor.getInt(1), cursor.getInt(0));
            HashMap<String, Integer> colors = new HashMap<>();

            for (int i = 3; i < factors.size() + 3; i++) {
                colors.put(factors.get(i - 3), cursor.getInt(i));
            }
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length - 1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        cursor.close();
        return entryMap;
    }

    @Override
    public HashMap<DateTime, DayEntry> getDatabaseEntriesWeek(List<String> factors, int startDay, int month, int year, int endDay
            , int endMonth, int endYear) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<DateTime, DayEntry> entryMap = new HashMap<>();
        String[] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length - 1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for (int i = 0; i < factors.size(); i++) {
            projection[3 + i] = factors.get(i);
        }

        String selection =
                "((" + SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY + " >= " + startDay + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + month + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + year + ") OR (" +

                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY + " <= " + endDay + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + endMonth + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + endYear + "))";


        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DateTime date = DateTime.forDateOnly(cursor.getInt(2), cursor.getInt(1), cursor.getInt(0));
            HashMap<String, Integer> colors = new HashMap<>();

            for (int i = 3; i < factors.size() + 3; i++) {
                colors.put(factors.get(i - 3), cursor.getInt(i));
            }
            Log.d("Für Tag", date + "");
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length - 1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();

        }
        cursor.close();
        return entryMap;
    }

    @Override
    public HashMap<String, String> getFactorsFromDatabase() {
        SQLiteDatabase dbRwad = getReadableDatabase();
        String[] projection = {
                SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS,
                SQLiteMethods.COLUMN_NAME_FACTOR_COLOR,
                SQLiteMethods.COLUMN_NAME_FACTOR_ENABLED
        };

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
        HashMap<String, String> factors = new HashMap<>();
        while (!cursor.isAfterLast()) {
            factors.put(cursor.getString(0), cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return factors;
    }


    public HashMap<String, Boolean> getFactorsEnabledFromDatabase() {
        SQLiteDatabase dbRwad = getReadableDatabase();
        String[] projection = {
                SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS,
                SQLiteMethods.COLUMN_NAME_FACTOR_COLOR,
                SQLiteMethods.COLUMN_NAME_FACTOR_ENABLED,
        };

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
        HashMap<String, Boolean> factors = new HashMap<>();
        while (!cursor.isAfterLast()) {
            int i =  cursor.getInt(2);
            boolean enabled = (i == 1)?true:false;
            factors.put(cursor.getString(0), enabled);
            cursor.moveToNext();
        }
        cursor.close();
        return factors;
    }

    @Override
    public List<String> getFactors() {
        SQLiteDatabase dbRwad = getReadableDatabase();
        String[] projection = {
                SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS,
        };

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
        List<String> factors = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            factors.add(cursor.getString(0));
            cursor.moveToNext();
        }cursor.close();
        return factors;
    }

    @Override
    public void saveReminder(NotificationModel reminder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        boolean[] bools = reminder.getActiveForDays();
        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_REMINDER_SUNDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_MONDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_TUESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_THURSDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_FRIDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_SATURDAY};
        values.put(SQLiteMethods.COLUMN_NAME_REMINDER_ID, reminder.getAlarmID());
        values.put(SQLiteMethods.COLUMN_NAME_REMINDER_DIALOG_ID, reminder.getDialogID());
        values.put(SQLiteMethods.COLUMN_NAME_REMINDER_TEXT, reminder.getText());
        values.put(SQLiteMethods.COLUMN_NAME_REMINDER_TIME, reminder.getTime());
        if (reminder.isActive()) values.put(SQLiteMethods.COLUMN_NAME_REMINDER_ACTIVE, 1);
        else values.put(SQLiteMethods.COLUMN_NAME_REMINDER_ACTIVE, 0);

        for (int i = 0; i < dayColumns.length && i < bools.length; i++) {
            if (bools[i]) values.put(dayColumns[i], 1);
            else values.put(dayColumns[i], 0);
        }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void removeReminder(int iD) {
        SQLiteDatabase db = getWritableDatabase();

        String UPDATE  = SQLiteMethods.UPDATE + SQLiteMethods.TABLE_NAME_REMINDER_TABLE +
                SQLiteMethods.SPACE + SQLiteMethods.SET + SQLiteMethods.COLUMN_NAME_REMINDER_TEXT + " = ''" +
                SQLiteMethods.WHERE + SQLiteMethods.COLUMN_NAME_REMINDER_ID + " = " + iD;
        db.execSQL(UPDATE);
    }

    @Override
    public List<NotificationModel> getAllReminders() {
        List<NotificationModel> reminders = new ArrayList<>();
        SQLiteDatabase dbRwad = getReadableDatabase();

        String[] projection = {
                SQLiteMethods.COLUMN_NAME_REMINDER_ID,
                SQLiteMethods.COLUMN_NAME_REMINDER_DIALOG_ID,
                SQLiteMethods.COLUMN_NAME_REMINDER_TEXT,
                SQLiteMethods.COLUMN_NAME_REMINDER_TIME,
                SQLiteMethods.COLUMN_NAME_REMINDER_ACTIVE,
                SQLiteMethods.COLUMN_NAME_REMINDER_SUNDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_MONDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_TUESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_THURSDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_FRIDAY,
                SQLiteMethods.COLUMN_NAME_REMINDER_SATURDAY
        };


        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                projection,
                null,
                null,
                null,
                null,
                SQLiteMethods.COLUMN_NAME_REMINDER_ID + " ASC"
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int alarmID = cursor.getInt(0);
            int dialogId = cursor.getInt(1);
            String text = cursor.getString(2);
            String time = cursor.getString(3);
            boolean isActive = (cursor.getInt(4) == 1);
            boolean[] activeForDays = new boolean[7];
            for (int i = 0; i < activeForDays.length; i++) {
                activeForDays[i] = (cursor.getInt(5 + i) == 1);
            }
            NotificationModel reminder = new NotificationModel(alarmID, dialogId, time, text, isActive, activeForDays);
            reminders.add(reminder);
            cursor.moveToNext();
        }
        cursor.close();
        return reminders;
    }

    @Override
    public String getWeatherData(DateTime dateTime) {
        SQLiteDatabase dbRwad = getReadableDatabase();

        String selection =
                "(" + SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY + " = " + dateTime.getDay() + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + dateTime.getMonth() + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + dateTime.getYear() + ")";
        String[] projection = {SQLiteMethods.COLUMN_NAME_WEATHER_WEATHER};

        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_WEATHER_TABLE,
                projection,
                selection,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        String result =  (cursor.getCount() > 0) ? cursor.getString(0): "";
        cursor.close();
        return result;
    }

    @Override
    public void deleteFactor(String factor, List<String> factors){
        SQLiteDatabase db = getWritableDatabase();

        String[] deleteWhere = new String[]{factor};
        db.delete(SQLiteMethods.TABLE_NAME_FACTOR_TABLE,  SQLiteMethods.COLUMN_NAME_FACTOR_ID_FACTORS + "=?" , deleteWhere);

        factors.remove(factor);
        String createTableCmd = SQLiteMethods.SQL_CREATE_ENTRIES;
        String tableName = SQLiteMethods.TABLE_NAME_MAIN_TABLE;
        try {
            dropColumn(db, createTableCmd , tableName , factor, factors);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getFactorList().remove(factor);
        getFactorEnabledMap().remove(factor);
        getFactorColorMap().remove(factor);
    }

    private void dropColumn(SQLiteDatabase db,
                            String createTableCmd,
                            String tableName,
                            String colToRemove,
                            List<String> remainingFactors ) throws java.sql.SQLException {

        List<String> updatedTableColumns = getTableColumns(tableName, db);
        // Remove the columns we don't want anymore from the table's list of columns
        updatedTableColumns.remove(colToRemove);

        String columnsSeperated = TextUtils.join(",", updatedTableColumns);

        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

        // Creating the table on its new format (no redundant columns)
        db.execSQL(createTableCmd);

        for(String factor : remainingFactors){
            db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, factor));
        }

        // Populating the table with the data
        db.execSQL("INSERT INTO " + tableName + "(" + columnsSeperated + ") SELECT "
                + columnsSeperated + " FROM " + tableName + "_old;");
        db.execSQL("DROP TABLE " + tableName + "_old;");
    }

    private List<String> getTableColumns(String tableName, SQLiteDatabase db) {
        ArrayList<String> columns = new ArrayList<String>();
        String cmd = "pragma table_info(" + tableName + ");";
        Cursor cur = db.rawQuery(cmd, null);

        while (cur.moveToNext()) {
            columns.add(cur.getString(cur.getColumnIndex("name")));
        }
        cur.close();

        return columns;
    }

    public List<String> getFactorList() {
        if(factorList == null)  factorList = getFactors();
        return factorList;
    }

    public HashMap<String, String> getFactorColorMap() {
        if(factorColorMap == null) factorColorMap = getFactorsFromDatabase();
        return factorColorMap;
    }

    public HashMap<String, Boolean> getFactorEnabledMap() {
        if(factorEnabledMap == null) factorEnabledMap = getFactorsEnabledFromDatabase();
        return factorEnabledMap;
    }

}



