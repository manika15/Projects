package com.example.database;


import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ToDoItemDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_USERID,
	      MySQLiteHelper.COLUMN_TODONAME,
	      MySQLiteHelper.COLUMN_TODONOTE,
	      MySQLiteHelper.COLUMN_DUETIME,
	      MySQLiteHelper.COLUMN_NODUETIME,
	      MySQLiteHelper.COLUMN_PRIORITY,
	      MySQLiteHelper.COLUMN_CHECKED,
	      MySQLiteHelper.COLUMN_TASKKEY,
	      MySQLiteHelper.COLUMN_CREATION,
	      MySQLiteHelper.COLUMN_DELETED,
	      };
	  

	  public ToDoItemDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public ToDoItem getItemByItemId(long id) {
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
	        null, null, null);
		    cursor.moveToFirst();
		    if (cursor.isAfterLast())
		    	return null;
		    ToDoItem newItem = cursorToToDoItem(cursor);
		    return newItem;
		    
		  
	  }
	  
	  public ToDoItem getItemByItemKey(String key) {
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
	        allColumns, MySQLiteHelper.COLUMN_TASKKEY + " ='" +key+"'", null,
	        null, null, null);
		    cursor.moveToFirst();
		    if (cursor.isAfterLast())
		    	return null;
		    ToDoItem newItem = cursorToToDoItem(cursor);
		    return newItem;
		    
		  
	  }
	  

	  public ToDoItem createItem(long userid, String name, String note,
			  long duetime, boolean noduetime, boolean checked, long priority) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_USERID, userid);
	    values.put(MySQLiteHelper.COLUMN_TODONAME, name);
	    values.put(MySQLiteHelper.COLUMN_TODONOTE, note);
	    values.put(MySQLiteHelper.COLUMN_DUETIME, duetime);
	    if (noduetime)
	    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
	    else
	    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
	    values.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
	    if (checked)
	    	values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
	    else
	    	values.put(MySQLiteHelper.COLUMN_CHECKED, 0);
	    	
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_TODOLIST, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    ToDoItem newItem = cursorToToDoItem(cursor);
	    cursor.close();
	    return newItem;
	  }
	  public ToDoItem createItem(String userid, String name, String note,
			  long duetime, boolean noduetime, boolean checked, long priority, String key, long creation) {
		  try{
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_USERID, userid);
	    values.put(MySQLiteHelper.COLUMN_TODONAME, name);
	    values.put(MySQLiteHelper.COLUMN_TODONOTE, note);
	    values.put(MySQLiteHelper.COLUMN_DUETIME, duetime);
	    if (noduetime)
	    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
	    else
	    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
	    values.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
	    if (checked)
	    	values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
	    else
	    	values.put(MySQLiteHelper.COLUMN_CHECKED, 0);
	    	
	    values.put(MySQLiteHelper.COLUMN_TASKKEY, key);
	    values.put(MySQLiteHelper.COLUMN_CREATION, creation);
	    values.put(MySQLiteHelper.COLUMN_DELETED, false);	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_TODOLIST, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    ToDoItem newItem = cursorToToDoItem(cursor);
	    cursor.close();
	    return newItem;
		  }
		  catch(Exception exp)
		  {
		  exp.printStackTrace();
		  return null;
		  }
	  }
	  
	  
	  public void deleteItem(ToDoItem item) {
		  //long id = item.getId();
		    String key = item.getEncodedKey();
		    database.delete(MySQLiteHelper.TABLE_TODOLIST, MySQLiteHelper.COLUMN_TASKKEY
	        + " ='"+key+"'", null);
	  }
	  
	  public int updateItem(ToDoItem item) {
		  //long id = item.getId();
		  String key = item.getEncodedKey();
		  
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_TODONAME, item.getName());
		    values.put(MySQLiteHelper.COLUMN_TODONOTE, item.getNote());
		    values.put(MySQLiteHelper.COLUMN_DUETIME, item.getDueTime());
		    //values.put(MySQLiteHelper.COLUMN_TASKKEY, item.getEncodedKey());
		    if (item.isNoDueTime()){
		    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		    }
		    else {
		    	values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		    }
		    values.put(MySQLiteHelper.COLUMN_PRIORITY, item.getPriority());
		    if (item.isChecked()) {
		    	values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		    }
		    else
		    {
		    	values.put(MySQLiteHelper.COLUMN_CHECKED, 0);
		    }
		    Log.d("inside update", "update is "+item.getCreationDate());
            values.put(MySQLiteHelper.COLUMN_CREATION, item.getCreationDate());
		    values.put(MySQLiteHelper.COLUMN_DELETED, item.getDeleted());
		  
		  return database.update(MySQLiteHelper.TABLE_TODOLIST, values, MySQLiteHelper.COLUMN_TASKKEY + " ='" +key+"'", null);
	  }

	  

	/*  public List<ToDoItem> getToDoListByUId(long userId) {
	    List<ToDoItem> list = new ArrayList<ToDoItem>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
	        allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId, 
	        null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	ToDoItem item = cursorToToDoItem(cursor);
	      list.add(item);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return list;
	  }
	  */
	  
	  public List<ToDoItem> getToDoListByUId(String userKey) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, MySQLiteHelper.COLUMN_USERID + " ='" + userKey+"' AND "+ MySQLiteHelper.COLUMN_DELETED +"=0", 
		        null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }

	/*  public List<ToDoItem> getInPendingListByUId(long userId) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId +" AND "+ MySQLiteHelper.COLUMN_CHECKED +"=1", 
		        null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }
	  */
	  public List<ToDoItem> getInPendingListByUId(String userKey) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, MySQLiteHelper.COLUMN_USERID + " ='" +userKey+"'"+" AND "+ MySQLiteHelper.COLUMN_CHECKED +"=1 AND "+ MySQLiteHelper.COLUMN_DELETED +"=0", 
		        null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }
	  
	  /*public List<ToDoItem> getInCompleteListByUId(long userId) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId +" AND "+ MySQLiteHelper.COLUMN_CHECKED +"=0", 
		        null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }
*/
	  public List<ToDoItem> getInCompleteListByUId(String userKey) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, MySQLiteHelper.COLUMN_USERID + " ='" +userKey+"'"+" AND "+ MySQLiteHelper.COLUMN_CHECKED +"=0 AND "+ MySQLiteHelper.COLUMN_DELETED +"=0", 
		        null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }
	  
	  public List<ToDoItem> getAllToDo() {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	ToDoItem item = cursorToToDoItem(cursor);
		      list.add(item);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return list;
		  }
	  
	  private ToDoItem cursorToToDoItem(Cursor cursor) {
		  ToDoItem item = new ToDoItem();
		  item.setId(cursor.getLong(0));
		  item.setUserId(cursor.getString(1));
		  item.setName(cursor.getString(2));
		  item.setNote(cursor.getString(3));
		  item.setDueTime(cursor.getLong(4));
		  item.setNoDueTime(cursor.getLong(5)==0?false:true);
		  item.setPriority(cursor.getLong(6));
		  item.setChecked(cursor.getLong(7)==0?false:true);
		  item.setEncodedKey(cursor.getString(8));
		  item.setCreationDate(cursor.getLong(9));
	    return item;
	  }
}
