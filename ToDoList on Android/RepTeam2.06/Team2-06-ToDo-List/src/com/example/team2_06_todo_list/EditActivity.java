package com.example.team2_06_todo_list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.example.database.DataSource;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	private DataSource datasource;

	// int fields to open coresponding dialog boxes for the dat and time fields
	static final int STARTDATE_DAILOG_ID = 0;
	Time startDateValue = new Time();
	long startDateFinalValue=0;
	Time CurrentDateValue = new Time();

	String user_id;
	String item_id;
	String str_sortingFlag;

	static final String ITEM_id = "item_id";
	static final String ITEM = "item";
	static final String DUE_DATE = "due_date";     
	static final String DESCRIPTION = "description";
	static final String PRIOITY = "priority";
	static final String STATUS = "status";
	static final String REFRENCE_ID = "reference_id";

	private static ArrayList<HashMap<String, String>> ToDoArrayList = new ArrayList<HashMap<String,String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		datasource = new DataSource(this);
		datasource.open();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		user_id = extras.getString("user_id");
		item_id = extras.getString("item_id");
		str_sortingFlag = extras.getString("str_sortingFlag");

		//set the start date to the current date
		final TextView startDateLabel = (TextView) findViewById(R.id.TextView_edit_StartDate);
		startDateValue.setToNow();
		startDateFinalValue = startDateValue.toMillis(true);
		startDateLabel.setText(DateFormat.format("MMMM dd, yyyy", startDateFinalValue));

		fillData();

		ImageButton pickStartDate = (ImageButton) findViewById(R.id.ImageButton_edit_DueDate);
		pickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(STARTDATE_DAILOG_ID);
			}
		});


		//set the start time to the current date
		//        final TextView startTimeLabel = (TextView) findViewById(R.id.TextView_CreateInvite_StartTime_Label);
		//        startTimeLabel.setText("(pick start time)");

		Button btn_save = (Button)findViewById(R.id.btn_edit_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!validate_Fields()){
					return;
				}
				add_item();
				Intent intent = new Intent(EditActivity.this, MainActivity.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("str_sortingFlag", str_sortingFlag);
				startActivity(intent);
			}
		});

		Button btn_cancel = (Button)findViewById(R.id.btn_edit_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		final Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_edit_Priority);
		spinner_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
					long selectedId) {

				ImageView img_icon = (ImageView)findViewById(R.id.image_edit_Priority);
				if(selectedItemPosition == 0)
				{
					img_icon.setImageResource(R.drawable.red);
				}
				else if(selectedItemPosition == 1)
				{
					img_icon.setImageResource(R.drawable.blue);
				}
				else if(selectedItemPosition == 2)
				{
					img_icon.setImageResource(R.drawable.yellow);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});



	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		getMenuInflater().inflate(R.menu.activity_main, menu);
	//		menu.findItem(R.id.menu_item_new).setIntent(new Intent(EditActivity.this, EditActivity.class));
	//		menu.findItem(R.id.menu_item_Hidden).setIntent(new Intent(EditActivity.this, HiddenItemsActivity.class));
	//		return true;
	//	}

	private Boolean validate_Fields ()
	{
		EditText txt_task = (EditText)findViewById(R.id.EditText_edit_task);

		String str_user = txt_task.getText().toString();

		if(str_user.equals(""))
		{
			showMessageToast(EditActivity.this, "Please enter task.!");
			return false;
		}

		int dateComparisonResult = Time.compare(startDateValue,CurrentDateValue);
		if(dateComparisonResult < 0) // date fields validation
		{
			showMessageToast(EditActivity.this,"You can not enter past date!");
			return false;
		}

		return true;
	}

	private void add_item()
	{
		EditText txt_item = (EditText)findViewById(R.id.EditText_edit_task);
		EditText txt_desc = (EditText)findViewById(R.id.edit_edit_Note);
		Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_edit_Priority);

		String str_Item = txt_item.getText().toString();
		String str_Desc = txt_desc.getText().toString();
		String DueDate = DateFormat.format("MM-dd-yyyy", startDateFinalValue).toString();
		String Priority = spinner_priority.getSelectedItem().toString();
		String str_priority = null;
		if(Priority.equals("High"))
		{
			str_priority = "1";
		}
		if(Priority.equals("Medium"))
		{
			str_priority = "2";
		}
		else if(Priority.equals("Low"))
		{
			str_priority = "3";
		}	
		datasource.UpdateItem(item_id, str_Item, DueDate, str_Desc, str_priority, null);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListenerForStartDate = new DatePickerDialog.OnDateSetListener()
	{
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			try
			{
				final TextView startDateLabel = (TextView) findViewById(R.id.TextView_edit_StartDate);

				startDateValue.set(dayOfMonth, monthOfYear, year);
				startDateFinalValue = startDateValue.toMillis(true);
				startDateLabel.setText(DateFormat.format("MMMM dd, yyyy", startDateFinalValue));

			}
			catch(Exception ex)
			{
				Log.v("Logged error : ", "TimePickerDialog.OnTimeSetListener in CreateNewInviteActivity, userid");
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);

		switch (id)
		{
		case STARTDATE_DAILOG_ID:  // for start date picker
			return new DatePickerDialog(this,  mDateSetListenerForStartDate,  cyear, cmonth, cday);

		}
		return null;
	}

	private void getData() {

		try
		{
			ToDoArrayList = new ArrayList<HashMap<String,String>>();
			DataSource mDbadapter = new DataSource(this);
			mDbadapter.open(); 
			Cursor data_cur = mDbadapter.fetchItemdetails(item_id);
			if(data_cur.getCount() >0)
			{ 
				if(data_cur.moveToFirst()) 
				{ 
					do   
					{
						HashMap<String,String> map = new HashMap<String, String>(); 

						String item = data_cur.getString(0);
						String due_date = data_cur.getString(1);     
						String description = data_cur.getString(2);
						String priority = data_cur.getString(3);
						String status = data_cur.getString(4);
						//							int refrence_id = data_cur.getInt(6);
						//							int sa = refrence_id;

						map.put(ITEM, item);
						map.put(DUE_DATE, due_date);
						map.put(DESCRIPTION, description);
						map.put(PRIOITY, priority);
						map.put(STATUS, status);
						//map.put(REFRENCE_ID, refrence_id);
						ToDoArrayList.add(map);

					}while (data_cur.moveToNext());

				}
			}

			else Toast.makeText(this, 
					"No Records", 
					Toast.LENGTH_SHORT).show();       


			data_cur.close();   
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void fillData()
	{
		getData();
		ImageView img_icon = (ImageView)findViewById(R.id.image_edit_Priority);
		EditText txt_item = (EditText)findViewById(R.id.EditText_edit_task);
		EditText txt_desc = (EditText)findViewById(R.id.edit_edit_Note);
		TextView txt_date = (TextView)findViewById(R.id.TextView_edit_StartDate);
		Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_edit_Priority);

		txt_item.setText(ToDoArrayList.get(0).get(ITEM));
		txt_desc.setText(ToDoArrayList.get(0).get(DESCRIPTION));
		txt_date.setText(ToDoArrayList.get(0).get(DUE_DATE));
		if(ToDoArrayList.get(0).get(PRIOITY).equals("High"))
		{
			spinner_priority.setSelection(0);
			img_icon.setImageResource(R.drawable.red);
		}
		else if(ToDoArrayList.get(0).get(PRIOITY).equals("Medium"))
		{
			spinner_priority.setSelection(1);
			img_icon.setImageResource(R.drawable.blue);
		}
		else
		{
			spinner_priority.setSelection(2);
			img_icon.setImageResource(R.drawable.yellow);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (datasource != null) {
			datasource.close();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (datasource != null) {
			datasource.close();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (datasource != null) {
			datasource.open();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {

		if (keyCode == KeyEvent.KEYCODE_BACK)  //Override Keyback to do nothing in this case.
		{
			try{
				EditText txt_task = (EditText)findViewById(R.id.EditText_edit_task);
				String str_user = txt_task.getText().toString();

				if(str_user.equals(""))
				{
					showMessageToast(EditActivity.this, "Please enter task.!");
					return false;

				}
				else
				{
					int dateComparisonResult = Time.compare(startDateValue, CurrentDateValue);
					if(dateComparisonResult < 0) // date fields validation
					{
						showMessageToast(EditActivity.this,"You can not enter past date!");
						return false;
					}
					add_item();
					Intent intent = new Intent(EditActivity.this, MainActivity.class);
					intent.putExtra("user_id", user_id);
					intent.putExtra("str_sortingFlag", str_sortingFlag);
					startActivity(intent);
				}



			}
			catch(Exception e)
			{

			}

		}
		return super.onKeyDown(keyCode, event);  //-->All others key will work as usual
	}

	public void showMessageToast(Activity activityId,String message) {
		try
		{
			Toast tst = Toast.makeText(activityId, message, Toast.LENGTH_LONG);
			tst.setGravity(Gravity.CENTER, tst.getXOffset() / 2, tst.getYOffset() / 2);
			tst.show();
		}
		catch(Exception ex)
		{
			Log.v("Logged error : ", "showMessageToast() in MeetingWaveMainClass, userid");

		}

		final Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_edit_Priority);
		spinner_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
					long selectedId) {

				ImageView img_icon = (ImageView)findViewById(R.id.image_edit_Priority);
				if(selectedItemPosition == 0)
				{
					img_icon.setImageResource(R.drawable.red);
				}
				else if(selectedItemPosition == 1)
				{
					img_icon.setImageResource(R.drawable.blue);
				}
				else if(selectedItemPosition == 2)
				{
					img_icon.setImageResource(R.drawable.yellow);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

}
