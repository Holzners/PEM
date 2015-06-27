package com.team3.pem.pem.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ReminderModel;
import com.team3.pem.pem.adapters.RemindersAdapter;

import java.util.List;


public class NotificationsActivity extends ActionBarActivity {

    FeedReaderDBHelper dbHelper;
    List<ReminderModel> reminders;
    Button new_reminder;
    EditText reminderBezeichnung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        dbHelper = FeedReaderDBHelper.getInstance();
        reminders = dbHelper.getAllReminders();

        final ListView listView = (ListView) findViewById(R.id.list_reminders);
        new_reminder = (Button) findViewById(R.id.create_reminder);
        reminderBezeichnung = (EditText) findViewById(R.id.reminder_bezeichnung);

        final RemindersAdapter adapter = new RemindersAdapter(this, R.layout.reminders_list_layout, reminders);
        listView.setAdapter(adapter);

        new_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderModel reminderModel = new ReminderModel(reminders.size(), reminders.size(), "08:00", reminderBezeichnung.getText().toString(),
                                                                false, new boolean[]{false, false, false, false, false, false, false});
                dbHelper.saveReminder(reminderModel);
                reminders = dbHelper.getAllReminders();
                adapter.clear();
                adapter.addAll(reminders);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}