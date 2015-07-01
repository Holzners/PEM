package com.team3.pem.pem.mSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team3.pem.pem.utili.ColorsToPick;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.ReminderModel;

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

        DateTime startDate = DateTime.forDateOnly(2013, 1, 1);
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
        values3.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, "Kopfschmerzen");
        values3.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, ColorsToPick.BLUE.name());

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values3, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values4 = new ContentValues();
        values4.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, "Bauchschmerzen");
        values4.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, ColorsToPick.RED.name());

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values4, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values5 = new ContentValues();
        values5.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, "Müdigkeit");
        values5.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, ColorsToPick.VIOLETTE.name());

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values5, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values10 = new ContentValues();

        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY};
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID, 0);
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_DIALOG_ID, 0);
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_TEXT, "Tag bewerten");
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_TIME, "08:00");
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 0);

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
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, factorName);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, colorId);
        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, factorName));

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
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_WEATHER, weatherData);

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
                colors.put(factors.get(i), cursor.getInt(i));
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
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS,
                SQLiteMethods.COLUMN_NAME_ENTRY_COLOR,
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

    @Override
    public List<String> getFactors() {
        SQLiteDatabase dbRwad = getReadableDatabase();
        String[] projection = {
                SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS,
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
    public void saveReminder(ReminderModel reminder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        boolean[] bools = reminder.getActiveForDays();
        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY};
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID, reminder.getAlarmID());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DIALOG_ID, reminder.getDialogID());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_TEXT, reminder.getText());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_TIME, reminder.getTime());
        if (reminder.isActive()) values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 1);
        else values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 0);

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
        String DELETE = SQLiteMethods.DELETE_FROM + SQLiteMethods.TABLE_NAME_REMINDER_TABLE +
                SQLiteMethods.SPACE + SQLiteMethods.SPACE + SQLiteMethods.WHERE +
                SQLiteMethods.COLUMN_NAME_ENTRY_ID + " = " + iD;
        db.execSQL(DELETE);

    }

    @Override
    public List<ReminderModel> getAllReminders() {
        List<ReminderModel> reminders = new ArrayList<>();
        SQLiteDatabase dbRwad = getReadableDatabase();

        String[] projection = {
                SQLiteMethods.COLUMN_NAME_ENTRY_ID,
                SQLiteMethods.COLUMN_NAME_ENTRY_DIALOG_ID,
                SQLiteMethods.COLUMN_NAME_ENTRY_TEXT,
                SQLiteMethods.COLUMN_NAME_ENTRY_TIME,
                SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE,
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY
        };


        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                projection,
                null,
                null,
                null,
                null,
                SQLiteMethods.COLUMN_NAME_ENTRY_ID + " ASC"
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
            ReminderModel reminder = new ReminderModel(alarmID, dialogId, time, text, isActive, activeForDays);
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
        String[] projection = {SQLiteMethods.COLUMN_NAME_ENTRY_WEATHER};

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
        String result =  cursor.getString(0);
        cursor.close();
        return result;
    }

}



