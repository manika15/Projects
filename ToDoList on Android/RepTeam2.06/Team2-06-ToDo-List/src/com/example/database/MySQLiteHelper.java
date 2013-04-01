package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_ToDoList = "todo";
	public static final String COLUMN_ID = "item_id";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_DUE_DATE = "duedate";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_PRIORITY = "priority";
	public static final String COLUMN_STATUS =  "status";
	public static final String COLUMN_Reference_id = "reference_id";

	public static final String TABLE_USER = "user";
	public static final String USER_ID = "user_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_EMAIL = "email";

	private static final String DATABASE_NAME = "todolist.db";
	private static final int DATABASE_VERSION = 8;

	// Database creation sql statement
	private static final String DATABASE_USER = "create table "
			+ TABLE_USER + "(" + USER_ID
			+ " integer primary key autoincrement, " + COLUMN_USERNAME
			+ " text not null, " + COLUMN_PASSWORD
			+ " text not null, " + COLUMN_EMAIL
			+ " null);";

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ToDoList + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_ITEM
			+ " text, " + COLUMN_DUE_DATE
			+ " text, " + COLUMN_DESC
			+ " text, " + COLUMN_PRIORITY
			+ " text, " + COLUMN_STATUS
			+ " text, " + COLUMN_Reference_id
			+ " integer not null);";
//			" reference_id INTEGER NOT NULL");";
//		    " FOREIGN KEY(reference_id) REFERENCES user(user_id) ON DELETE CASCADE" +
//		    ");";


	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_USER);
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ToDoList);
		onCreate(db);
	}

} 