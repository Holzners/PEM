package com.team3.pem.pem.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.RemindersAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ReminderModel;

import java.util.List;


public class NotificationsActivity extends ActionBarActivity {

    FeedReaderDBHelper dbHelper;
    List<ReminderModel> reminders;
    FloatingActionButton new_reminder;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        dbHelper = FeedReaderDBHelper.getInstance();
        reminders = dbHelper.getAllReminders();

        listView = (ListView) findViewById(R.id.list_reminders);
        new_reminder = (FloatingActionButton) findViewById(R.id.newReminder);

        final RemindersAdapter adapter = new RemindersAdapter(this, R.layout.reminders_list_layout, reminders);
        listView.setAdapter(adapter);

        new_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(NotificationsActivity.this);
                View promptsView = li.inflate(R.layout.new_reminder_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        NotificationsActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.newReminderField);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        ReminderModel reminderModel = new ReminderModel(reminders.size(), reminders.size(), "08:00", userInput.getText().toString(),
                                                false, new boolean[]{false, false, false, false, false, false, false});
                                        dbHelper.saveReminder(reminderModel);
                                        reminders = dbHelper.getAllReminders();
                                        adapter.clear();
                                        adapter.addAll(reminders);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
