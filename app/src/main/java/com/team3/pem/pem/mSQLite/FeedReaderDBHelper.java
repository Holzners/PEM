package com.team3.pem.pem.mSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team3.pem.pem.R;

import java.util.Date;
import java.util.List;

import utili.SQLiteMethods;

/**
 * Created by Stephan on 16.06.15.
 */
public class FeedReaderDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MedicalJournal.db";

    public FeedReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteMethods.SQL_CREATE_ENTRIES);
        db.execSQL(SQLiteMethods.SQL_CREATE_FACTORS);
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Kopfschmerzen"));
        db.execSQL(SQLiteMethods.addColumn(SQLiteMethods.TABLE_NAME_MAIN_TABLE, "Bauchschmerzen"));


        ContentValues values = new ContentValues();
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 12);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_MONTH, 6);
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_YEAR, 2015);

        values.put("Kopfschmerzen", (R.color.blue));
        values.put("Bauchschmerzen", (R.color.red));
        values.put(SQLiteMethods.COLUMN_NAME_ENTRY_DESCRIPTION, "Damn hard day");

        db.insertWithOnConflict(
                SQLiteMethods.TABLE_NAME_MAIN_TABLE,
                "null", values, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues values2 = new ContentValues();
        values2.put(SQLiteMethods.COLUMN_NAME_ENTRY_ID_DAY, 11);
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

    public void saveDay(Date date, List<Integer> ratings, String text){
        SQLiteDatabase db = getWritableDatabase();
    }

}



