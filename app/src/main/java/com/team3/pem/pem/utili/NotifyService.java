package com.team3.pem.pem.utili;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.MainActivity;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Chris on 23.06.2015.
 */
public class NotifyService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final int ID = intent.getIntExtra("id", 0);

        FeedReaderDBHelper dbHelper = FeedReaderDBHelper.getInstance();
        List<NotificationModel> reminders = dbHelper.getAllReminders();

        boolean[] days = reminders.get(ID).getActiveForDays();

        Calendar calendar = Calendar.getInstance();
        DateTime date = DateTime.today(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //Tag bewerten erinnnerung
        if(ID == 0) {
            List<String> factors = dbHelper.getFactorList();
            DayEntry factorsDay = dbHelper.getDatabaseEntriesDay(factors, date.getDay(), date.getMonth(), date.getYear());
            if(factorsDay != null){
                return;
            }
        }

        if(!days[dayOfWeek]){
            return;
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_action_alarms)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText("Medical Journal");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, mBuilder.build());
    }
}
