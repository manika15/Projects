package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ITEM,
			MySQLiteHelper.COLUMN_DUE_DATE,
			MySQLiteHelper.COLUMN_DESC };

	private String[] allUserColumns = { MySQLiteHelper.USER_ID,
			MySQLiteHelper.COLUMN_USERNAME,
			MySQLiteHelper.COLUMN_PASSWORD,
			MySQLiteHelper.COLUMN_EMAIL };

	public DataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ToDoItems createItem(String item, String date, String decription, String priority, String refernce_id, String status) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ITEM, item);
		values.put(MySQLiteHelper.COLUMN_DUE_DATE, date);
		values.put(MySQLiteHelper.COLUMN_DESC, decription);
		values.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
		values.put(MySQLiteHelper.COLUMN_STATUS, status);
		values.put(MySQLiteHelper.COLUMN_Reference_id, refernce_id);
		long insertId = database.insert(MySQLiteHelper.TABLE_ToDoList, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ToDoList,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);																
		cursor.moveToFirst();
		ToDoItems newItem = cursorToItems(cursor);
		cursor.close();
		return newItem;
	}

	public UserItem createUser(String user, String password, String email) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USERNAME, user);
		values.put(MySQLiteHelper.COLUMN_PASSWORD, password);
		values.put(MySQLiteHelper.COLUMN_EMAIL, email);
		long insertId = database.insert(MySQLiteHelper.TABLE_USER, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
				allUserColumns, MySQLiteHelper.USER_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		UserItem newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}

	public boolean UpdateItem(String id, String item, String due_date, String description, String priority, String status) {
		ContentValues cv = new ContentValues();
		if (item != null)
			cv.put(MySQLiteHelper.COLUMN_ITEM, item);
		if (due_date != null)
			cv.put(MySQLiteHelper.COLUMN_DUE_DATE, due_date);
		if (due_date != null)
			cv.put(MySQLiteHelper.COLUMN_DESC, description);
		if (priority != null)
			cv.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
		if (status != null)
			cv.put(MySQLiteHelper.COLUMN_STATUS, status);
		return database.update(MySQLiteHelper.TABLE_ToDoList, cv, MySQLiteHelper.COLUMN_ID + " = " + id, null) > 0;
	}

	public void deleteItem(String id) {
		//	    long id = item.getId();
		System.out.println("Item deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ToDoList, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public Cursor fetchAllItems(String id) {
		String query = "SELECT item_id, item, duedate, description, priority, status FROM todo where reference_id = " + id + " and status = 1 order by duedate desc";
		return database.rawQuery(query,null);
	}
	
	public Cursor fetchAllHiddenItems(String id) {
		String query = "SELECT item_id, item, duedate, description, priority, status FROM todo where reference_id = " + id + " and status = 0 order by duedate desc";
		return database.rawQuery(query,null);
	}

	public Cursor fetchAllItemsSortedByPriority(String id) {
		String query = "SELECT * FROM todo where reference_id = " + id + " order by priority asc";
		return database.rawQuery(query,null);
	}

	public Cursor fetchItemdetails(String id) {
		String query = "SELECT item, duedate, description, priority, status FROM todo where item_id = " + id;
		return database.rawQuery(query,null);
	}

	public Cursor fetchAllUsers() {
		String query = "SELECT username, user_id FROM user";
		return database.rawQuery(query,null);


	}
	public Cursor SetStatus(String id) {
		String query = "UPDATE todo SET status = 0 WHERE item_id = " + id;
		return database.rawQuery(query,null);

	}

	public Cursor getUserInfo() {
		String query = "SELECT user_id, username, password FROM user";
		return database.rawQuery(query,null);


	}

	public Cursor getItems() {

		String query = "SELECT user_id, username, password FROM user";
		return database.rawQuery(query,null);


	}

	//	  public List<ToDoItems> getAllComments() {
	//	    List<ToDoItems> items = new ArrayList<ToDoItems>();
	//
	//	    Cursor cursor = database.query(MySQLiteHelper.TABLE_ToDoList,
	//	        allColumns, null, null, null, null, null);
	//
	//	    cursor.moveToFirst();
	//	    while (!cursor.isAfterLast()) {
	//	      ToDoItems item = cursorToItems(cursor);
	//	      items.add(item);
	//	      cursor.moveToNext();
	//	    }
	//	    // Make sure to close the cursor
	//	    cursor.close();
	//	    return items;
	//	  }

	private ToDoItems cursorToItems(Cursor cursor) {
		ToDoItems item = new ToDoItems();
		item.setId(cursor.getLong(0));
		item.setToDoItem(cursor.getString(1));
		return item;
	}

	private UserItem cursorToUser(Cursor cursor) {
		UserItem item = new UserItem();
		item.setId(cursor.getLong(0));
		item.setUserItem(cursor.getString(1));
		return item;
	}
}
