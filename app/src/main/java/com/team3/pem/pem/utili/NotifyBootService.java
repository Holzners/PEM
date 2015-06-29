package com.team3.pem.pem.utili;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Chris on 29.06.2015.
 */
public class NotifyBootService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            FeedReaderDBHelper dbHelper = FeedReaderDBHelper.getInstance();
            List<ReminderModel> reminders = dbHelper.getAllReminders();
            for(ReminderModel reminder : reminders){
                if(reminders.get(reminder.getAlarmID()).isActive()) {
                    int hour = Integer.parseInt(reminder.getTime().substring(0, 2));
                    int minute = Integer.parseInt(reminder.getTime().substring(3));
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent notifyIntent = new Intent(context, NotifyService.class);
                    notifyIntent.putExtra("title", reminder.getText());
                    notifyIntent.putExtra("id", reminder.getAlarmID());
                    PendingIntent pIntent = PendingIntent.getBroadcast(context, reminder.getAlarmID(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    //Set the alarm to start at specified time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pIntent);
                }
            }
        }
    }
}
