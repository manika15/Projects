package com.example.cs6300todolist;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.database.ToDoItem;
import com.example.database.ToDoItemDataSource;

@TargetApi(9)
public class EditItemActivity extends Activity {

    private ToDoItemDataSource itemdatasource;
    private long userId = 0;
    private long itemId = 0;
    private String userKey = null;
    private boolean checked;
    private String itemKey = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
    //    userId = getIntent().getLongExtra("com.example.cs6300todolist.userid",
      //          0);
        userKey = getIntent().getStringExtra("com.example.cs6300todolist.userkey");
      //  itemId = getIntent().getLongExtra("com.example.cs6300todolist.itemid",
         //       0);
        itemKey = getIntent().getStringExtra("com.example.cs6300todolist.itemkey");    
        itemdatasource = new ToDoItemDataSource(this);
        itemdatasource.open();
        final EditText itemNameEditText = (EditText) findViewById(R.id.editText41);
        final EditText itemNoteEditText = (EditText) findViewById(R.id.editText42);
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.checkBox41);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.datePicker41);
        final SeekBar priorityBar = (SeekBar) findViewById(R.id.seekBar41);
        if (itemKey == null) {
            this.setTitle("Add New Item");
            setDueTimeCheckBox.setChecked(false);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 2);
            dueDatePicker.updateDate(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            priorityBar.setProgress(0);
            dueDatePicker.setVisibility(View.GONE);
        } else {
            this.setTitle("Edit Item");
            ToDoItem item = itemdatasource.getItemByItemKey(itemKey);
            if (item == null) {
                Toast toast = Toast.makeText(this, "Task does not exist.",
                        Toast.LENGTH_LONG);
                toast.show();
                finish();
                this.setResult(RESULT_CANCELED);
                return;
            }
            itemNameEditText.setText(item.getName());
            itemNoteEditText.setText(item.getNote());
            setDueTimeCheckBox.setChecked(!item.isNoDueTime());
            checked = item.isChecked();
            // finishCheckBox.setChecked(item.isChecked());
            Calendar cal = Calendar.getInstance();
            if (item.isNoDueTime()) {
                cal.add(Calendar.DAY_OF_MONTH, 2);
            } else {
                cal.setTimeInMillis(item.getDueTime());
            }
            dueDatePicker.updateDate(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            priorityBar.setProgress((int) item.getPriority());
            if (item.isNoDueTime()) {
                dueDatePicker.setVisibility(View.GONE);
            } else {
                dueDatePicker.setVisibility(View.VISIBLE);
            }
        }
        setDueTimeCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  //  @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (buttonView.isChecked()) {
                            dueDatePicker.setVisibility(View.VISIBLE);
                        } else {
                            dueDatePicker.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveClick(View view) throws JSONException, InterruptedException, ExecutionException {
        final EditText itemNameEditText = (EditText) findViewById(R.id.editText41);
        final EditText itemNoteEditText = (EditText) findViewById(R.id.editText42);
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.checkBox41);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.datePicker41);
        final SeekBar priorityBar = (SeekBar) findViewById(R.id.seekBar41);
        // save data here
        if (itemNameEditText.getText().toString().isEmpty()) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Task Creation Error");
            alertDialog.setMessage("Task name cannot be empty.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        if (itemKey == null) {
            // Add item
            String name = itemNameEditText.getText().toString();
            if (name.isEmpty()) {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Task Creation Error");
                alertDialog.setMessage("Task name cannot be empty.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            String note = itemNoteEditText.getText().toString();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Task Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            
            Date d = new Date();
            long creationDate = d.getTime();

            long duetime = cal.getTimeInMillis();
            JSONObject addJson = new JSONObject();
            addJson.put("userId",userKey);
            addJson.put("name", name);
            addJson.put("note", note);
            addJson.put("dueTime", duetime);
            addJson.put("noDueTime", !setDueTimeCheckBox.isChecked());
            addJson.put("checked", false);
            addJson.put("priority", priorityBar.getProgress());
            addJson.put("creationDate", creationDate);
            addJson.put("deleted", false);
            
            Log.d("AddTask", "Added task is"+addJson.toString());
            Communicator comm = new Communicator(addJson.toString(), "http://www.sdptaskstodo.appspot.com/api/task", "POST");
            String addResponse = comm.execute().get();
            Log.d("AddTask", "After adding the task is "+addResponse);
            JSONObject addedJson = new JSONObject(addResponse);
            String addEncodedKey = addedJson.getString("encodedKey");
            
            ToDoItem item = itemdatasource.createItem(userKey, name, note,
                    duetime, !setDueTimeCheckBox.isChecked(), false,
                    priorityBar.getProgress(), addEncodedKey, creationDate);
            if (item != null) {
                Toast toast = Toast.makeText(this, "Task created.",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            // Update item
            ToDoItem item = new ToDoItem();
            item.setName(itemNameEditText.getText().toString());
            item.setNote(itemNoteEditText.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Task Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
          
            Date dedit = new Date();
            long editcreationDate = dedit.getTime();
            
            item.setDueTime(cal.getTimeInMillis());
            item.setId(itemId);
            item.setUserId(userKey);
            item.setNoDueTime(!setDueTimeCheckBox.isChecked());
            item.setChecked(checked);
            item.setPriority(priorityBar.getProgress());
            item.setDeleted(true);
            Log.d("Edit", "Creation date is "+editcreationDate);
            item.setCreationDate(editcreationDate);
            Log.d("JI", "after setting creation date is "+item.getCreationDate());
            item.setEncodedKey(itemKey);
            itemdatasource.updateItem(item);
            Toast toast = Toast.makeText(this, "Task updated.",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        this.setResult(RESULT_OK);
        this.finish();
    }

    public void onCancelClick(View view) {
        Toast toast = Toast
                .makeText(this, "Cancel Editing.", Toast.LENGTH_LONG);
        toast.show();
        this.setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        final EditText itemNameEditText = (EditText) findViewById(R.id.editText41);
        final EditText itemNoteEditText = (EditText) findViewById(R.id.editText42);
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.checkBox41);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.datePicker41);
        final SeekBar priorityBar = (SeekBar) findViewById(R.id.seekBar41);
        // save data here
        if (itemId == 0) {
            // Add item
            String name = itemNameEditText.getText().toString();
            String note = itemNoteEditText.getText().toString();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Task Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                          //      @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            long duetime = cal.getTimeInMillis();
            ToDoItem item = itemdatasource.createItem(userId, name, note,
                    duetime, !setDueTimeCheckBox.isChecked(), false,
                    priorityBar.getProgress());
            if (item != null) {
                Toast toast = Toast.makeText(this, "Task created." + userId,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            // Update item
            ToDoItem item = new ToDoItem();
            item.setName(itemNameEditText.getText().toString());
            item.setNote(itemNoteEditText.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Task Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                              //  @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            item.setDueTime(cal.getTimeInMillis());
            item.setId(itemId);
            item.setUserId(userKey);
            item.setNoDueTime(!setDueTimeCheckBox.isChecked());
            item.setChecked(checked);
            item.setPriority(priorityBar.getProgress());
            itemdatasource.updateItem(item);
            Toast toast = Toast.makeText(this, "Task updated.",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        this.setResult(RESULT_OK);
        this.finish();
        super.onBackPressed();
    }
}
