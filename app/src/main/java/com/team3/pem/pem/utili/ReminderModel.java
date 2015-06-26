package com.team3.pem.pem.utili;

import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

/**
 * Created by Stephan on 25.06.15.
 */
public class ReminderModel {

    private int dialogID;

    private String time;

    private boolean isActive;

    // 0 = Mo , 1 = Di, 2 = MI, 3 = Do, 4 = Fr, 5 = Sa, 6 = So
    private boolean[] activeForDays;

    private int alarmID;

    private String text;

    private FeedReaderDBHelper dbHelper;

    public ReminderModel( int alarmID,int dialogID , String time, String text, boolean isActive, boolean[] activeForDays) {
        this.dialogID = dialogID;
        this.time = time;
        this.isActive = isActive;
        this.activeForDays = activeForDays;
        this.alarmID = alarmID;
        this.text = text;
        dbHelper = FeedReaderDBHelper.getInstance();
    }

    public int getDialogID() {
        return dialogID;
    }

    public void setDialogID(int dialogID) {
        this.dialogID = dialogID;
        dbHelper.saveReminder(this);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        dbHelper.saveReminder(this);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
        dbHelper.saveReminder(this);
    }

    public boolean[] getActiveForDays() {
        return activeForDays;
    }

    public void setActiveDay(int i, boolean bool){
        this.activeForDays[i] = bool;
        dbHelper.saveReminder(this);
    }

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
        dbHelper.saveReminder(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        dbHelper.saveReminder(this);
    }
}
