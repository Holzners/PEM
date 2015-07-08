package com.team3.pem.pem.mSQLite;

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
    public static final String OPEN_BRACE = " (";
    public static final String CLOSE_BRACE = ") ";
    public static final String UPDATE = "UPDATE ";
    public static final String SET = "SET ";
    public static final String WHERE = "WHERE ";

    public static final String TABLE_NAME_MAIN_TABLE = "entries";
    public static final String COLUMN_NAME_ENTRY_ID_DAY = "DAY";
    public static final String COLUMN_NAME_ENTRY_ID_MONTH = "MONTH";
    public static final String COLUMN_NAME_ENTRY_ID_YEAR = "YEAR";
    public static final String COLUMN_NAME_ENTRY_DESCRIPTION = "Description";

    public static final String TABLE_NAME_WEATHER_TABLE = "Weathertable";
    public static final String COLUMN_NAME_WEATHER_WEATHER = "Weather";

    public static final String TABLE_NAME_FACTOR_TABLE = "factors";
    public static final String COLUMN_NAME_FACTOR_ID_FACTORS = "factor";
    public static final String COLUMN_NAME_FACTOR_ENABLED = "isEnabled";
    public static final String COLUMN_NAME_FACTOR_COLOR = "COLOR";
    public static final String COLUMN_NAME_FACTOR_IS_GRADUAL = "gradual";

    public static final String TABLE_NAME_REMINDER_TABLE = "REMINDERS";
    public static final String COLUMN_NAME_REMINDER_TIME = "TIME";
    public static final String COLUMN_NAME_REMINDER_SUNDAY = "SUNDAY";
    public static final String COLUMN_NAME_REMINDER_MONDAY = "MONDAY";
    public static final String COLUMN_NAME_REMINDER_TUESDAY = "TUESDAY";
    public static final String COLUMN_NAME_REMINDER_WEDNESDAY = "WEDNESDAY";
    public static final String COLUMN_NAME_REMINDER_THURSDAY = "THURSDAY";
    public static final String COLUMN_NAME_REMINDER_FRIDAY = "FRIDAY";
    public static final String COLUMN_NAME_REMINDER_SATURDAY = "SATURDAY";
    public static final String COLUMN_NAME_REMINDER_ACTIVE = "ACTIVE";
    public static final String COLUMN_NAME_REMINDER_TEXT = "REMINDERTEXT";
    public static final String COLUMN_NAME_REMINDER_DIALOG_ID = "DIALOGID";
    public static final String COLUMN_NAME_REMINDER_ID = "ALARMID";

    public static final String SQL_CREATE_REMINDERS =
            CREATE_TABLE + SPACE + TABLE_NAME_REMINDER_TABLE + OPEN_BRACE +
                    COLUMN_NAME_REMINDER_ID + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_REMINDER_ACTIVE + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_SUNDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_MONDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_TUESDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_WEDNESDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_THURSDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_FRIDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_SATURDAY + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_DIALOG_ID + SPACE + NUMBER_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_TEXT + SPACE + TEXT_TYPE +COMMA_STEP +
                    COLUMN_NAME_REMINDER_TIME + SPACE + TEXT_TYPE +COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE + COLUMN_NAME_REMINDER_ID +CLOSE_BRACE +
                    CLOSE_BRACE;

    public static final String SQL_CREATE_ENTRIES =
            CREATE_TABLE + SPACE +TABLE_NAME_MAIN_TABLE + OPEN_BRACE +
                    COLUMN_NAME_ENTRY_ID_DAY + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_YEAR + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_DESCRIPTION + SPACE + TEXT_TYPE + COMMA_STEP +
                    COLUMN_NAME_WEATHER_WEATHER + SPACE + TEXT_TYPE + COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE + COLUMN_NAME_ENTRY_ID_DAY + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + COMMA_STEP + COLUMN_NAME_ENTRY_ID_YEAR + CLOSE_BRACE +
                    CLOSE_BRACE;

    public static final String SQL_CREATE_WEATHER =
            CREATE_TABLE + SPACE +TABLE_NAME_WEATHER_TABLE + OPEN_BRACE +
                    COLUMN_NAME_ENTRY_ID_DAY + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_YEAR + SPACE + NUMBER_TYPE + COMMA_STEP +
                    COLUMN_NAME_WEATHER_WEATHER + SPACE + TEXT_TYPE + COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE + COLUMN_NAME_ENTRY_ID_DAY + COMMA_STEP +
                    COLUMN_NAME_ENTRY_ID_MONTH + COMMA_STEP + COLUMN_NAME_ENTRY_ID_YEAR + CLOSE_BRACE +
                    CLOSE_BRACE;

    public static final String SQL_CREATE_FACTORS =
                    CREATE_TABLE+ SPACE + TABLE_NAME_FACTOR_TABLE + OPEN_BRACE +
                            COLUMN_NAME_FACTOR_ID_FACTORS + SPACE + TEXT_TYPE + COMMA_STEP +
                            COLUMN_NAME_FACTOR_COLOR + SPACE + TEXT_TYPE + COMMA_STEP +
                            COLUMN_NAME_FACTOR_ENABLED + SPACE + NUMBER_TYPE + COMMA_STEP +
                            COLUMN_NAME_FACTOR_IS_GRADUAL + SPACE + NUMBER_TYPE + COMMA_STEP +
                    PRIMARY_KEY + OPEN_BRACE+ COLUMN_NAME_FACTOR_ID_FACTORS + CLOSE_BRACE + CLOSE_BRACE;


    public static String addColumn(String tableName, String columnName){
        return ALTER_TABLE + SPACE + tableName + " ADD COLUMN" + SPACE + columnName + SPACE +TEXT_TYPE;
    }

}
