package com.team3.pem.pem.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.team3.pem.pem.R;
import com.team3.pem.pem.activities.NotificationsActivity;
import com.team3.pem.pem.utili.NotifyService;
import com.team3.pem.pem.utili.NotificationModel;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Chris on 26.06.2015.
 */
public class NotificationsAdapter extends ArrayAdapter {

    private NotificationsActivity context;
    private List<NotificationModel> reminders;

    public NotificationsAdapter(NotificationsActivity context, int resource, List<NotificationModel> reminders) {
        super(context, resource, reminders);
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public NotificationModel getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationModel reminderModel = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_reminder_layout, parent, false);

        //Init all IDs of the layout
        TextView time = (TextView) convertView.findViewById(R.id.zeit);
        Switch reminderSwitch = (Switch) convertView.findViewById(R.id.reminderSwitch);
        TextView[] days = new TextView[7];
        days[0] = (TextView) convertView.findViewById(R.id.so);
        days[1] = (TextView) convertView.findViewById(R.id.mo);
        days[2] = (TextView) convertView.findViewById(R.id.di);
        days[3] = (TextView) convertView.findViewById(R.id.mi);
        days[4] = (TextView) convertView.findViewById(R.id.don);
        days[5] = (TextView) convertView.findViewById(R.id.fr);
        days[6] = (TextView) convertView.findViewById(R.id.sa);

        time.setText(reminderModel.getTime());
        reminderSwitch.setText(reminderModel.getText());
        if(reminderModel.isActive())
            reminderSwitch.setChecked(true);

        //Get which days in a week are activated and change their colors
        for(int i = 0; i < reminderModel.getActiveForDays().length; i++){
            if(reminderModel.getActiveForDays()[i])
                days[i].setTextColor(Color.rgb(0, 150, 255));
            days[i].setOnClickListener(new ReminderViewListener(days, i, reminderModel));
        }

        time.setOnClickListener(new ReminderViewListener(time, reminderModel));
        reminderSwitch.setOnCheckedChangeListener(new ReminderViewListener(reminderModel));

        return convertView;
    }

    public class ReminderViewListener implements View.OnClickListener, OnCheckedChangeListener{

        private TextView time;
        private TextView[] days;
        private int index;
        private NotificationModel reminderModel;
        private int hour, minute;

        /**
         * ClickListener for clicking on a day in a week
         *
         * @param days
         * @param index
         * @param reminder
         */
        public ReminderViewListener(TextView[] days, int index, NotificationModel reminder) {
            this.days = days;
            this.index = index;
            reminderModel = reminder;
        }

        /**
         * ClickListener for clicking on the time
         *
         * @param time
         * @param reminder
         */
        public ReminderViewListener(TextView time, NotificationModel reminder) {
            this.time = time;
            reminderModel = reminder;
        }

        /**
         * CheckedChangeListener for enabling the notification
         *
         * @param reminder
         */
        public ReminderViewListener(NotificationModel reminder) {
            reminderModel = reminder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.zeit:
                    //Converting hour and minute to integer and open TimePickerDialog
                    hour = Integer.parseInt(reminderModel.getTime().substring(0, 2));
                    minute = Integer.parseInt(reminderModel.getTime().substring(3));
                    TimePickerDialog tp = new TimePickerDialog(context, timePickerListener, hour, minute, true);
                    tp.show();
                    break;
                case R.id.mo:
                case R.id.di:
                case R.id.mi:
                case R.id.don:
                case R.id.fr:
                case R.id.sa:
                case R.id.so:
                    //Changing text colors and recreating the alarm
                    if (reminderModel.getActiveForDays()[index]) {
                        days[index].setTextColor(Color.argb(255, 109, 109, 109));
                        reminderModel.setActiveDay(index, false);
                    } else {
                        days[index].setTextColor(Color.rgb(0, 150, 255));
                        reminderModel.setActiveDay(index, true);
                    }
                    if(reminderModel.isActive())
                        createAlarm(reminderModel.getAlarmID());
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId() != R.id.reminderSwitch) {
                return;
            }
            //Creating or canceling the alarm
            if (isChecked) {
                reminderModel.setIsActive(true);
                createAlarm(reminderModel.getAlarmID());
            } else {
                reminderModel.setIsActive(false);
                cancelAlarm(reminderModel.getAlarmID());
            }
        }

        //Creating TimePickerDialog and recreating the alarm
        private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeString = (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
                time.setText(timeString);

                reminderModel.setTime(timeString);
                if(reminderModel.isActive())
                    createAlarm(reminderModel.getAlarmID());
            }
        };

        /**
         *  Create and set the AlarmManager for this notification
         *
         *  @param alarmID  The unique ID of the notification
         */
        private void createAlarm(int alarmID){
            hour = Integer.parseInt(reminderModel.getTime().substring(0, 2));
            minute = Integer.parseInt(reminderModel.getTime().substring(3));
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotifyService.class);
            intent.putExtra("title", reminderModel.getText() );
            intent.putExtra("id", alarmID);

            PendingIntent pIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Set the alarm to start at specified time
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pIntent);
            Log.i("alarm", "Created alarm " + alarmID);
        }
    }

    /**
     * Cancel and delete a set alarm
     *
     * @param alarmID
     */
    public void cancelAlarm(int alarmID) {
        Intent intent = new Intent(context, NotifyService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.i("alarm", "Canceled alarm " + alarmID);
    }
}
