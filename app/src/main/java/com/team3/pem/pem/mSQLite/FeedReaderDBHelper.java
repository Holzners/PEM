package com.team3.pem.pem.mSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.ReminderModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static FeedReaderDBHelper getInstance(){
        if(mdbHelper != null) return mdbHelper;
        return new FeedReaderDBHelper(appContext);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteMethods.SQL_CREATE_ENTRIES);
        db.execSQL(SQLiteMethods.SQL_CREATE_FACTORS);
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Kopfschmerzen"));
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Bauchschmerzen"));
        db.execSQL(SQLiteMethods.SQL_CREATE_REMINDERS);
/*
        ContentValues values = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 22);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, 6);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, 2015);
        values.put("Kopfschmerzen", 1);
        values.put("Bauchschmerzen", 3);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values2 = new ContentValues();
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 21);
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, 6);
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, 2015);

        values2.put("Kopfschmerzen", 4);
        values2.put("Bauchschmerzen", 3);
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values2, SQLiteDatabase.CONFLICT_REPLACE);

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

        ContentValues values10  = new ContentValues();

        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY};
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID, 1);
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_DIALOG_ID, 2);
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_TEXT,  "Some Text");
        values10.put(SQLiteMethods.COLUMN_NAME_ENTRY_TIME, "8:00");
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 1);

        for (int i = 0; i< dayColumns.length; i++){
            values10.put(dayColumns[i], 1);
        }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                "null", values10, SQLiteDatabase.CONFLICT_REPLACE);
                */
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void saveFactor(String factorName, String colorId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, factorName);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, colorId);
        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    @Override
    public void saveDay(Date date, HashMap<String,Integer> ratings, String text){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = new ContentValues();

        Log.d("Saved: " , text+ date);

        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, date.getDate());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, date.getMonth());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, date.getYear());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, text);

            for (Map.Entry<String, Integer> e : ratings.entrySet()){
                Log.d(e.getKey(), e.getValue()+"");
                values.put(e.getKey(),e.getValue());
            }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);


    }
    @Override
    public HashMap<Date, DayEntry>  getDatabaseEntries(List<String> factors){
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<Date, DayEntry> entryMap = new HashMap<>();

        String [] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length-1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for(int i = 0 ; i < factors.size() ; i++){
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

        while (!cursor.isAfterLast()){
            Date date = new Date (cursor.getInt(2), cursor.getInt(1) , cursor.getInt(0));
            List<Integer> colors = new ArrayList<>();

            for(int i = 3 ; i < factors.size() +3; i++){
                colors.add(cursor.getInt(i));
            }
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length-1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        return entryMap;
    }

    @Override
    public HashMap<Date, DayEntry> getDatabaseEntriesDay(List<String> factors, int day, int month, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<Date, DayEntry> entryMap = new HashMap<>();
        String [] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length-1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for(int i = 0 ; i < factors.size() ; i++){
            projection[3 + i] = factors.get(i);
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

        while (!cursor.isAfterLast()){
            Date date = new Date (cursor.getInt(2), cursor.getInt(1) , cursor.getInt(0));
            List<Integer> colors = new ArrayList<>();

            for(int i = 3 ; i < factors.size() +3; i++){
                colors.add(cursor.getInt(i));
                Log.d("Farbe", cursor.getInt(i) + "");
            }
            Log.d("Für Tag", date +"");
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length-1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();

        }
        return entryMap;
    }

    @Override
    public HashMap<Date, DayEntry> getDatabaseEntriesMonth(List<String> factors, int month, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<Date, DayEntry> entryMap = new HashMap<>();
        String [] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length-1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for(int i = 0 ; i < factors.size() ; i++){
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

        while (!cursor.isAfterLast()){
            Date date = new Date (cursor.getInt(2), cursor.getInt(1) , cursor.getInt(0));
            List<Integer> colors = new ArrayList<>();

            for(int i = 3 ; i < factors.size() +3; i++){
                colors.add(cursor.getInt(i));
            }
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length-1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        return entryMap;
    }

    @Override
    public HashMap<Date, DayEntry> getDatabaseEntriesYear(List<String> factors, int year) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<Date, DayEntry> entryMap = new HashMap<>();
        String [] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length-1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for(int i = 0 ; i < factors.size() ; i++){
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

        while (!cursor.isAfterLast()){
            Date date = new Date (cursor.getInt(2), cursor.getInt(1) , cursor.getInt(0));
            List<Integer> colors = new ArrayList<>();

            for(int i = 3 ; i < factors.size() +3; i++){
                colors.add(cursor.getInt(i));
            }
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length-1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();
        }
        return entryMap;
    }

    @Override
    public HashMap<Date, DayEntry> getDatabaseEntriesWeek(List<String> factors, int startDay, int month, int year, int endDay
    , int endMonth , int endYear) {
        SQLiteDatabase dbRwad = getReadableDatabase();
        HashMap<Date, DayEntry> entryMap = new HashMap<>();
        String [] projection = new String[4 + factors.size()];
        projection[0] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY;
        projection[1] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH;
        projection[2] = SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR;
        projection[projection.length-1] = SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION;

        for(int i = 0 ; i < factors.size() ; i++){
            projection[3 + i] = factors.get(i);
        }

        String selection =
                 "((" + SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY + " >= " + startDay + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH + " = " + month + " AND " +
                        SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR + " = " + year +  ") OR (" +

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

        while (!cursor.isAfterLast()){
            Date date = new Date (cursor.getInt(2), cursor.getInt(1) , cursor.getInt(0));
            List<Integer> colors = new ArrayList<>();

            for(int i = 3 ; i < factors.size() +3; i++){
                colors.add(cursor.getInt(i));
                Log.d("Farbe", cursor.getInt(i) + "");
            }
            Log.d("Für Tag", date +"");
            DayEntry descriptionColorMap = new DayEntry(colors, cursor.getString(projection.length-1));
            entryMap.put(date, descriptionColorMap);
            cursor.moveToNext();

        }
        return entryMap;
    }

    @Override
    public HashMap<String, String> getFactorsFromDatabase(){
        SQLiteDatabase dbRwad = getReadableDatabase();
        String [] projection = {
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
        HashMap<String , String> factors = new HashMap<>();
        while (!cursor.isAfterLast()){
            factors.put(cursor.getString(0), cursor.getString(1));
            cursor.moveToNext();
        }
        return factors;
    }
    @Override
    public List<String> getFactors () {
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
        }
        return factors;
    }

    @Override
    public void saveReminder(ReminderModel reminder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = new ContentValues();

        boolean[] bools = reminder.getActiveForDays();
        String[] dayColumns = {
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY};
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID, reminder.getAlarmID());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DIALOG_ID, reminder.getDialogID());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_TEXT,  reminder.getText());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_TIME, reminder.getTime());
        if(reminder.isActive()) values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 1);
        else   values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ACTIVE, 1);

        for (int i = 0; i< dayColumns.length && i < bools.length; i++){
            if(bools[i]) values.put(dayColumns[i], 1);
            else values.put(dayColumns[i], 0);
        }

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void removeReminder(int iD) {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE = SQLiteMethods.DELETE_FROM+SQLiteMethods.TABLE_NAME_REMINDER_TABLE+
                SQLiteMethods.SPACE + SQLiteMethods.SPACE + SQLiteMethods.WHERE +
                SQLiteMethods.COLUMN_NAME_ENTRY_ID + " = "+ iD;
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
                SQLiteMethods.COLUMN_NAME_ENTRY_MONDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_TUESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_WEDNESDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_THURSDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_FRIDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SATURDAY,
                SQLiteMethods.COLUMN_NAME_ENTRY_SUNDAY
        };


        Cursor cursor = dbRwad.query(
                SQLiteMethods.TABLE_NAME_REMINDER_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){

            int alarmID = cursor.getInt(0);
            int dialogId= cursor.getInt(1);
            String text = cursor.getString(2);
            String time = cursor.getString(3);
            boolean isActive = (cursor.getInt(4) == 1);
            boolean[] activeForDays = new boolean[7];
            for(int i = 0; i < activeForDays.length; i++ ){
                activeForDays[i] = (cursor.getInt(5+i) == 1);
            }
            ReminderModel reminder = new ReminderModel(alarmID,dialogId,time,text,isActive,activeForDays);
            reminders.add(reminder);
            cursor.moveToNext();
        }

        return reminders;
    }

}



