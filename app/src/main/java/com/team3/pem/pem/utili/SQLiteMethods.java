package com.team3.pem.pem.utili;

import java.util.Date;
import java.util.List;

/**
 * Created by Stephan on 16.06.15.
 */
public class SQLiteMethods {

    public static final String TEXT_TYPE = "varchar";
    public static final String NUMBER_TYPE = "int";
    public static final String SPACE = " ";
    public static final String COMMA_STEP = ", ";
    public static final String ALTER_TABLE = "ALTER TABLE";
    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String INSERT_INTO = "INSERT INTO";
    public static final String VALUES = "VALUES";
    public static final String OPEN_BRACE = " (";
    public static final String CLOSE_BRACE = ") ";
    public static final String DROP_TABLE = "DROP TABLE";

    public static final String TABLE_NAME_MAIN_TABLE = "entries";
    public static final String COLUMN_NAME_ENTRY_ID_DAY = "DAY";
    public static final String COLUMN_NAME_ENTRY_ID_MONTH = "MONTH";
    public static final String COLUMN_NAME_ENTRY_ID_YEAR = "YEAR";
    public static final String COLUMN_NAME_ENTRY_DESCRIPTION = "Description";

    public static final String TABLE_NAME_FACTOR_TABLE = "factors";
    public static final String COLUMN_NAME_ENTRY_ID_FACTORS = "FACTOR";
    public static final String COLUMN_NAME_ENTRY_COLOR = "COLOR";


    public static final String SQL_CREATE_ENTRIES =
            CREATE_TABLE + SPACE +TABLE_NAME_MAIN_TABLE + OPEN_BRACE +
                    COLUMN_NAME_ENTRY_ID_DAY + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_YEAR + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_DESCRIPTION + SPACE + TEXT_TYPE + COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE + COLUMN_NAME_ENTRY_ID_DAY + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + COMMA_STEP + COLUMN_NAME_ENTRY_ID_YEAR + CLOSE_BRACE +
                    CLOSE_BRACE;

    public static final String SQL_CREATE_FACTORS =
                    CREATE_TABLE+ SPACE + TABLE_NAME_FACTOR_TABLE + OPEN_BRACE +
                    COLUMN_NAME_ENTRY_ID_FACTORS + SPACE + TEXT_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_COLOR + SPACE + NUMBER_TYPE + COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE+ COLUMN_NAME_ENTRY_ID_FACTORS + CLOSE_BRACE + CLOSE_BRACE;

    public static String addColumn(String tableName, String columnName){
        return ALTER_TABLE + SPACE + tableName + " ADD COLUMN" + SPACE + columnName + SPACE +TEXT_TYPE;
    }

    public static String dropColumn(String tableName, String columnName){
        return ALTER_TABLE + SPACE +  tableName  +"REMOVE COLUMN" + SPACE + columnName + SPACE +TEXT_TYPE;
    }

    public static String addFactor(String factor , int color){
        return INSERT_INTO + SPACE + TABLE_NAME_FACTOR_TABLE + OPEN_BRACE + COLUMN_NAME_ENTRY_ID_FACTORS +
                COMMA_STEP + COLUMN_NAME_ENTRY_COLOR + CLOSE_BRACE + VALUES + OPEN_BRACE + factor +
                COMMA_STEP + color +CLOSE_BRACE;
    }

    public static String DROP_TABLE_ENTRIES = DROP_TABLE + SPACE + TABLE_NAME_MAIN_TABLE;
    public static String DROP_TABLE_FACTORS = DROP_TABLE + SPACE + TABLE_NAME_FACTOR_TABLE;

    public static String addDayEntry(Date date , List<Integer> ratings , String description){

        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();
        String valueString = "";
        for(Integer i : ratings){
            if(ratings.indexOf(i) < ratings.size() -1) valueString =+ i + ", ";
            else valueString =+ i+"";
        }

        String selectAllFactorsString = "SELECT " + COLUMN_NAME_ENTRY_ID_FACTORS + SPACE +
                                        "FROM " + TABLE_NAME_FACTOR_TABLE + ";";

        return INSERT_INTO + SPACE + TABLE_NAME_MAIN_TABLE + OPEN_BRACE +
                COLUMN_NAME_ENTRY_ID_DAY + COMMA_STEP + COLUMN_NAME_ENTRY_ID_MONTH + COMMA_STEP +
                COLUMN_NAME_ENTRY_ID_YEAR + COMMA_STEP + selectAllFactorsString  + COMMA_STEP +
                COLUMN_NAME_ENTRY_DESCRIPTION + CLOSE_BRACE +
                VALUES + OPEN_BRACE + day +COMMA_STEP + month + COMMA_STEP+ year + COMMA_STEP +
                valueString + COMMA_STEP + description;
    }
}
