package com.team3.pem.pem.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.RemindersAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.ReminderModel;

import java.util.Iterator;
import java.util.List;


public class NotificationsActivity extends ActionBarActivity {

    FeedReaderDBHelper dbHelper;
    List<ReminderModel> reminders;
    FloatingActionButton new_reminder;
    SwipeMenuListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        dbHelper = FeedReaderDBHelper.getInstance();
        reminders = getReminders(dbHelper.getAllReminders());

        listView = (SwipeMenuListView) findViewById(R.id.list_reminders);
        new_reminder = (FloatingActionButton) findViewById(R.id.newReminder);

        final RemindersAdapter adapter = new RemindersAdapter(this, R.layout.reminders_list_layout, reminders);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.TRANSPARENT));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_action_discard);
                swipeMenu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //  ApplicationInfo item = mAppList.get(position);
                ReminderModel item = adapter.getItem(position);
                switch (index) {
                    case 0:
                        //delete
                        if(item.getAlarmID() == 0){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantdelete), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        dbHelper.removeReminder(item.getAlarmID());
                        adapter.cancelAlarm(item.getAlarmID());
                        reminders = getReminders(dbHelper.getAllReminders());
                        adapter.clear();
                        adapter.addAll(reminders);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        new_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(NotificationsActivity.this);
                View promptsView = li.inflate(R.layout.new_reminder_layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        NotificationsActivity.this);

                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok), null);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.newReminderField);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (userInput.getText().toString().equals("")){
                                    Toast.makeText(NotificationsActivity.this, getResources().getString(R.string.noUserInput), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                int alarmID = 0;
                                for(int i = 0; i < dbHelper.getAllReminders().size(); i++){
                                    if(dbHelper.getAllReminders().get(i).getText().equals("")) {
                                        alarmID = i;
                                        break;
                                    }else{
                                        alarmID++;
                                    }
                                }
                                ReminderModel reminderModel = new ReminderModel(alarmID, alarmID, "08:00", userInput.getText().toString(),
                                        false, new boolean[]{false, false, false, false, false, false, false});
                                dbHelper.saveReminder(reminderModel);
                                reminders = getReminders(dbHelper.getAllReminders());
                                adapter.clear();
                                adapter.addAll(reminders);
                                adapter.notifyDataSetChanged();
                                Log.i("reminder", "Created reminder with alarmID " + alarmID);
                                dialog.dismiss();
                            }
                        });
                    }
                });

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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private List<ReminderModel> getReminders(List<ReminderModel> reminders){
        for (Iterator<ReminderModel> it = reminders.iterator(); it.hasNext(); ) {
            ReminderModel reminderModel = it.next();
            if(reminderModel.getText().equals("")){
                it.remove();
            }
        }
        return reminders;
    }
}
