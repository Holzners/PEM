package com.team3.pem.pem.mSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team3.pem.pem.R;
import com.team3.pem.pem.utili.DayEntry;
import com.team3.pem.pem.utili.IDatabaseHelper;
import com.team3.pem.pem.utili.SQLiteMethods;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephan on 16.06.15.
 */
public class FeedReaderDBHelper extends SQLiteOpenHelper implements IDatabaseHelper {

    public static Context appContext;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MedicalJournal.db";

    public static FeedReaderDBHelper mdbHelper;

    public FeedReaderDBHelper(Context context) {
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

        ContentValues values = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 22);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, 6);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, 2015);
        values.put("Kopfschmerzen", (R.color.blue));
        values.put("Bauchschmerzen", (R.color.red));
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values2 = new ContentValues();
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 21);
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, 6);
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, 2015);

        values2.put("Kopfschmerzen", (R.color.navy));
        values2.put("Bauchschmerzen", (R.color.accentColor));
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values2, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values3 = new ContentValues();
        values3.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, "Kopfschmerzen");
        values3.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, (R.color.blue));

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values3, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values4 = new ContentValues();
        values4.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, "Bauchschmerzen");
        values4.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, (R.color.red));

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values4, SQLiteDatabase.CONFLICT_REPLACE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void saveFactor(String factorName, int colorId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_FACTORS, factorName);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_COLOR, colorId);
        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_FACTOR_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    @Override
    public void saveDay(Date date, List<Integer> ratings, String text){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values  = new ContentValues();

        List<String> factors = getFactors();

        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, date.getDate());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, date.getMonth());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, date.getYear());
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, text);

        if(ratings.size() == factors.size()){
            for (int i = 0 ; i < ratings.size(); i++){
                values.put(factors.get(i), ratings.get(i));
            }
        }else{
            Log.e("Save to Database" , "Error while saving: Ratings and Factors Size dont match!");
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
    public HashMap<String, Integer> getFactorsFromDatabase(){
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
        HashMap<String , Integer> factors = new HashMap<>();
        while (!cursor.isAfterLast()){
            factors.put(cursor.getString(0), cursor.getInt(1));
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

}



