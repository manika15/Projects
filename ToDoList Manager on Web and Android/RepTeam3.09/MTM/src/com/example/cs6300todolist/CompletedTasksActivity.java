package com.example.cs6300todolist;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import com.example.database.DueDateComparator;
import com.example.database.PriorityComparator;
import com.example.database.ToDoItem;
import com.example.database.ToDoItemDataSource;

@TargetApi(5)
public class CompletedTasksActivity extends Activity{
    private ToDoItemDataSource itemdatasource;
    private long userId = 0;
    private String userKey = null;
    private static final int SORT_DUEDATE = 0;
    private static final int SORT_PRIORITY = 1;
    private int sortType = 0;
    private boolean hide = false;
    private String userName = null;
    
    public List<ToDoItem> getNewList() {
        List<ToDoItem> newList = null;
        if (itemdatasource == null) {
            return null;
        }
        
            newList = itemdatasource.getInPendingListByUId(userKey);
        
        if (sortType == SORT_DUEDATE) {
            Collections.sort(newList, new DueDateComparator());
        } else {
            Collections.sort(newList, new PriorityComparator());
        }
        return newList;
    }

    public boolean getHide() {
        return this.hide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedtasks);
      //  userId = getIntent().getLongExtra("com.example.cs6300todolist.userid",
        //        0);
        userKey = getIntent().getStringExtra("com.example.cs6300todolist.userkey");
        userName = getIntent().getStringExtra(
                "com.example.cs6300todolist.username");
        if (userKey == null) {
            finish();
            return;
        }
        this.setTitle("ToDo list for user " + userName);
        hide = false;
        sortType = SORT_DUEDATE;
        itemdatasource = new ToDoItemDataSource(this);
        itemdatasource.open();
        final List<ToDoItem> todoList = getNewList();

        final ListView toDoListView = (ListView) findViewById(R.id.completed_taskListView);
        ArrayAdapter<ToDoItem> adapter = new ToDoListAdapter(this, todoList,
                itemdatasource, 2);
        toDoListView.setClickable(true);
        toDoListView.setLongClickable(true);
        toDoListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @SuppressLint("NewApi")
         //   @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                final ToDoItem currentItem = todoList.get(position);
                PopupMenu popupMenu = new PopupMenu(CompletedTasksActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.list_popup_menu,
                        popupMenu.getMenu());
                popupMenu
                        .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                           // @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.edit_menu) {
                                    // edit
                                    Intent myIntent = new Intent(
                                            CompletedTasksActivity.this,
                                            EditItemActivity.class);
            //                        myIntent.putExtra(
              //                              "com.example.cs6300todolist.userid",
                //                            userId);
                                    myIntent.putExtra("com.example.cs6300todolist.userkey",
                                    		userKey);
                                    myIntent.putExtra("com.example.cs6300todolist.itemkey",
                                    		currentItem.getEncodedKey());
                                    myIntent.putExtra(
                                            "com.example.cs6300todolist.itemid",
                                            currentItem.getId());
                                    startActivityForResult(myIntent, 0);
                                } else {
                                    // delete
                                	currentItem.setDeleted(true);
        							itemdatasource.updateItem(currentItem);
                                    List<ToDoItem> todoList = getNewList();
                                    final ListView toDoListView = (ListView) findViewById(R.id.completed_taskListView);
                                    ToDoListAdapter adapter = (ToDoListAdapter) toDoListView
                                            .getAdapter();
                                    adapter.updateList(todoList);
                                    adapter.notifyDataSetChanged();
                                }
                                return true;
                            }
                        });
                popupMenu.show();
                return true;
            }
        });
        toDoListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_alltasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_duedate:
            sortType = SORT_DUEDATE;
            List<ToDoItem> todoList = getNewList();
            final ListView toDoListView = (ListView) findViewById(R.id.completed_taskListView);
            ToDoListAdapter adapter = (ToDoListAdapter) toDoListView
                    .getAdapter();
            adapter.updateList(todoList);
            adapter.notifyDataSetChanged();
            return true;
        case R.id.menu_priority:
            sortType = SORT_PRIORITY;
            List<ToDoItem> todoListp = getNewList();
            final ListView toDoListViewp = (ListView) findViewById(R.id.completed_taskListView);
            ToDoListAdapter adapterp = (ToDoListAdapter) toDoListViewp
                    .getAdapter();
            adapterp.updateList(todoListp);
            adapterp.notifyDataSetChanged();
            return true;
        case R.id.menu_changepwd:
            Intent myIntent = new Intent(this, AddUserActivity.class);
          //  myIntent.putExtra("com.example.cs6300todolist.userid", userId);
            myIntent.putExtra("com.example.cs6300todolist.userkey", userKey);
            startActivityForResult(myIntent, 0);
            return true;
        case R.id.menu_logout:
            setResult(RESULT_OK);
            finish();
            return true;
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.setResult(RESULT_OK);
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<ToDoItem> todoList = getNewList();
            final ListView toDoListView = (ListView) findViewById(R.id.completed_taskListView);
            ToDoListAdapter adapter = (ToDoListAdapter) toDoListView
                    .getAdapter();
            adapter.updateList(todoList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onAddTaskClick(View view) {
        Intent myIntent = new Intent(view.getContext(), EditItemActivity.class);
   //     myIntent.putExtra("com.example.cs6300todolist.userid", userId);
        myIntent.putExtra("com.example.cs6300todolist.userkey", userKey);
        myIntent.putExtra("com.example.cs6300todolist.tab_index", 2);
        startActivityForResult(myIntent, 0);
    }
    
    public void onSyncClick(View view) throws InterruptedException, ExecutionException, JSONException {

		Log.d("Sync","Inside sync");
		String message = "?username="+userName;
		
		List<ToDoItem> syncMobileList = null;
		
		if (itemdatasource == null) {
			syncMobileList = null;
		}
		if (hide) {
			syncMobileList = itemdatasource.getInCompleteListByUId(userKey);
		} else {
			syncMobileList = itemdatasource.getToDoListByUId(userKey);
		}
		if (sortType == SORT_DUEDATE) {
			Collections.sort(syncMobileList, new DueDateComparator());
		} else {
			Collections.sort(syncMobileList, new PriorityComparator());
		}
		Communicator syncComm = new Communicator(message, "http://www.sdptaskstodo.appspot.com/api/task"+message, "GET");
		String syncResponse = syncComm.execute().get();
		Log.d("Sync","message from server is "+syncResponse);
		JSONArray syncJsonArray = new JSONArray(syncResponse);
		
		
		for(Integer index = 0; index < syncJsonArray.length(); index++)
		{
			JSONObject syncJsonObject = syncJsonArray.getJSONObject(index);
			Log.d("Sync", index +" object is "+syncJsonObject.toString());
			String syncTaskname = syncJsonObject.getString("name");    		
			String syncTaskKey = syncJsonObject.getString("encodedKey");
			String syncTaskuserId = syncJsonObject.getString("userId");
			String syncNote = syncJsonObject.getString("note");
			long syncDueTime = syncJsonObject.getLong("dueTime");
			boolean syncnoDueTime = syncJsonObject.getBoolean("noDueTime");
			boolean syncChecked = syncJsonObject.getBoolean("checked");
			long syncPriority = syncJsonObject.getLong("priority");
			long syncCreation = syncJsonObject.getLong("creationDate");
			boolean syncDeleted = syncJsonObject.getBoolean("deleted");
			boolean syncFoundFlag = false;
			
			for(Integer inner = 0; inner < syncMobileList.size(); inner++)
			{			
				ToDoItem syncMobileItem = syncMobileList.get(inner);
				String syncMobileKey = syncMobileItem.getEncodedKey();
				long syncMobileCreation = syncMobileItem.getCreationDate();
				boolean syncMobileDeleted = syncMobileItem.getDeleted();
				if(syncMobileKey.equals(syncTaskKey)) {
					syncFoundFlag = true;
					if (syncMobileCreation < syncCreation) {
						Log.d("Sync", "To be updated in mobile local database");
						ToDoItem updateItem = new ToDoItem();
						updateItem.setName(syncTaskname);
						updateItem.setUserId(syncTaskuserId);
						updateItem.setNote(syncNote);
						updateItem.setDueTime(syncDueTime);
						updateItem.setNoDueTime(syncnoDueTime);
						updateItem.setChecked(syncChecked);
						updateItem.setPriority(syncPriority);
						updateItem.setEncodedKey(syncMobileKey);
						updateItem.setCreationDate(syncCreation);
						if((syncDeleted == true) || (syncMobileDeleted == true))
						{
							itemdatasource.deleteItem(updateItem);
							String deleteMessage = "?encodedkey="+syncTaskKey;
							Communicator deleteCommWeb = new Communicator(deleteMessage, "http://www.sdptaskstodo.appspot.com/api/task"+deleteMessage ,"DELETE");
							String deleteResponse = deleteCommWeb.execute().get();
							Log.d ("Delete", "Deleted task is "+deleteResponse);
						}
						else
						{						
							updateItem.setDeleted(false);
							itemdatasource.updateItem(updateItem);
						}
					}		
					else if (syncMobileCreation > syncCreation) {
						Log.d("Sync", "To be updated in central server");
						JSONObject updateWeb = new JSONObject();
						updateWeb.put("userId",syncMobileItem.getUserId());
						updateWeb.put("name", syncMobileItem.getName());
						updateWeb.put("note", syncMobileItem.getNote());
						updateWeb.put("dueTime", syncMobileItem.getDueTime());
						updateWeb.put("noDueTime", syncMobileItem.isNoDueTime());
						updateWeb.put("checked", syncMobileItem.isChecked());
						updateWeb.put("priority", syncMobileItem.getPriority());
						updateWeb.put("creationDate", syncMobileItem.getCreationDate());
						updateWeb.put("encodedKey", syncTaskKey);
						if((true == syncMobileItem.getDeleted()) || (syncDeleted == true))
						{
							syncMobileItem.setDeleted(true);
							itemdatasource.deleteItem(syncMobileItem);
							String deleteMobWebMessage = "?encodedkey="+syncTaskKey;
							Communicator deleteMobWeb = new Communicator(deleteMobWebMessage, "http://www.sdptaskstodo.appspot.com/api/task"+deleteMobWebMessage ,"DELETE");
							String deleteMobWebResponse = deleteMobWeb.execute().get();
							Log.d("Delete", "Deleted task is "+deleteMobWebResponse);
						}
						else
						{	
							updateWeb.put("deleted", false);
							Communicator updateWebComm = new Communicator(updateWeb.toString(), "http://www.sdptaskstodo.appspot.com/api/task", "PUT");
							String updateWebResponse = updateWebComm.execute().get();
							Log.d("JI", "updated web response is "+updateWebResponse);
						}
					}
				}
			}
			if(syncFoundFlag == false)
			{
				Log.d("Sync", "Task not present in local database. So create new one");
				ToDoItem syncMobileAddeditem = itemdatasource.createItem(syncTaskuserId, syncTaskname, syncNote,
						syncDueTime, syncnoDueTime, syncChecked,
						syncPriority, syncTaskKey, syncCreation);
			}
		}
	}
}



