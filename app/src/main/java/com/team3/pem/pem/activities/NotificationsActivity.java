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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.team3.pem.pem.R;
import com.team3.pem.pem.adapters.NotificationsAdapter;
import com.team3.pem.pem.mSQLite.FeedReaderDBHelper;
import com.team3.pem.pem.utili.NotificationModel;

import java.util.Iterator;
import java.util.List;


public class NotificationsActivity extends ActionBarActivity {

    private FeedReaderDBHelper dbHelper;
    private List<NotificationModel> reminders;
    private NotificationsAdapter adapter;
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        dbHelper = FeedReaderDBHelper.getInstance();
        reminders = getReminders(dbHelper.getAllReminders());

        SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.list_reminders);

        adapter = new NotificationsAdapter(this, R.layout.row_reminder_layout, reminders);
        listView.setAdapter(adapter);

        ImageView imageview = new ImageView(this);
        imageview.setImageResource(R.drawable.plus);

        //Init FloatingActionButton
//        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
//                .setBackgroundDrawable(R.drawable.button_action_accent_selector)
//                .setContentView(imageview)
//                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                showRateDayPopup(date);
//            }
//        });

        //Create SwipeMenuListener for delete and edit
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                //edit
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(R.color.accentColor));
                editItem.setWidth(dp2px(90));
                editItem.setIcon(R.drawable.ic_create_white_36dp);
                swipeMenu.addMenuItem(editItem);
                //delete
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 0, 0)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
                swipeMenu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        //Add ClickListener for edit and delete
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //  ApplicationInfo item = mAppList.get(position);
                final NotificationModel item = adapter.getItem(position);
                switch (index) {
                    case 0:
                        //edit
                        if (item.getAlarmID() == 0) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantedit), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        final AlertDialog alertDialog = createDialog();
                        userInput.setText(item.getText());
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(final DialogInterface dialog) {
                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (userInput.getText().toString().equals("")) {
                                            Toast.makeText(NotificationsActivity.this, getResources().getString(R.string.noUserInput), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        NotificationModel reminderModel = new NotificationModel(item.getAlarmID(), item.getAlarmID(), item.getTime(), userInput.getText().toString(),
                                                item.isActive(), item.getActiveForDays());
                                        dbHelper.saveReminder(reminderModel);
                                        reminders = getReminders(dbHelper.getAllReminders());
                                        adapter.clear();
                                        adapter.addAll(reminders);
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                        break;
                    case 1:
                        //delete
                        if (item.getAlarmID() == 0) {
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

        //OnClickListener for FAB to create new notification
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = createDialog();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (userInput.getText().toString().equals("")) {
                                    Toast.makeText(NotificationsActivity.this, getResources().getString(R.string.noUserInput), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                int alarmID = 0;
                                for (int i = 0; i < dbHelper.getAllReminders().size(); i++) {
                                    if (dbHelper.getAllReminders().get(i).getText().equals("")) {
                                        alarmID = i;
                                        break;
                                    } else {
                                        alarmID++;
                                    }
                                }
                                NotificationModel reminderModel = new NotificationModel(alarmID, alarmID, "08:00", userInput.getText().toString(),
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
        return super.onCreateOptionsMenu(menu);
    }



    /**
     *  Converts the given @param to an Integer
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * Removes all reminders from the given list which have no description
     *
     * @param reminders
     * @return the new remindersList
     */
    private List<NotificationModel> getReminders(List<NotificationModel> reminders){
        for (Iterator<NotificationModel> it = reminders.iterator(); it.hasNext(); ) {
            NotificationModel reminderModel = it.next();
            if(reminderModel.getText().equals("")){
                it.remove();
            }
        }
        return reminders;
    }

    /**
     * Create the dialog when creating or editing a notification
     *
     * @return the created dialog
     */
    private AlertDialog createDialog(){
        LayoutInflater li = LayoutInflater.from(NotificationsActivity.this);
        View promptsView = li.inflate(R.layout.dialog_new_notification, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                NotificationsActivity.this);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.action_ok), null);
        alertDialogBuilder.setView(promptsView);

        userInput = (EditText) promptsView
                .findViewById(R.id.newReminderField);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.action_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.action_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        return alertDialogBuilder.create();
    }
}
