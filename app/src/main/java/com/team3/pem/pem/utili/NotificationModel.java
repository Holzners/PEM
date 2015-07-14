package com.team3.pem.pem.utili;

import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

/**
 * Created by Stephan on 25.06.15.
 */
public class NotificationModel {

    private int dialogID;
    private String time;
    private boolean isActive;
    private boolean[] activeForDays; // 0 = Mo , 1 = Di, 2 = MI, 3 = Do, 4 = Fr, 5 = Sa, 6 = So
    private int alarmID;
    private String text;
    private FeedReaderDBHelper dbHelper;

    public NotificationModel(int alarmID, int dialogID, String time, String text, boolean isActive, boolean[] activeForDays) {
        this.dialogID = dialogID;
        this.time = time;
        this.isActive = isActive;
        this.activeForDays = activeForDays;
        this.alarmID = alarmID;
        this.text = text;
        this.dbHelper = FeedReaderDBHelper.getInstance();
    }

    /**
     * Get the dialogID of the given notification
     *
     * @return
     */
    public int getDialogID() {
        return dialogID;
    }

    /**
     * Get the time of the given notification
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * Set the time of the given notification
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
        dbHelper.saveReminder(this);
    }

    /**
     * Get the activation status of the given notification
     *
     * @return
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set the activation status of the given notification
     *
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
        dbHelper.saveReminder(this);
    }

    /**
     * Get active days of the given notification
     *
     * @return
     */
    public boolean[] getActiveForDays() {
        return activeForDays;
    }

    /**
     * Set the active days of the given notification
     *
     * @param i
     * @param bool
     */
    public void setActiveDay(int i, boolean bool){
        this.activeForDays[i] = bool;
        dbHelper.saveReminder(this);
    }

    /**
     * Get the alarmID of the given notification
     *
     * @return
     */
    public int getAlarmID() {
        return alarmID;
    }

    /**
     * Get the title of the given notification
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Set the title of the given notification
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        dbHelper.saveReminder(this);
    }
}
