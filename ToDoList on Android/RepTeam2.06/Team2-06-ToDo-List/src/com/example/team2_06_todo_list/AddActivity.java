package com.example.team2_06_todo_list;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataSource;

public class AddActivity extends Activity {

	private DataSource datasource;

	// int fields to open coresponding dialog boxes for the dat and time fields
	static final int STARTDATE_DAILOG_ID = 0;
	Time startDateValue = new Time();
	Time CurrentDateValue = new Time();
	long startDateFinalValue=0;

	String user_id;
	String str_sortingFlag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		datasource = new DataSource(this);
		datasource.open();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		user_id = extras.getString("user_id");
		str_sortingFlag = extras.getString("str_sortingFlag");

		ImageButton pickStartDate = (ImageButton) findViewById(R.id.ImageButton_add_DueDate);
		pickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(STARTDATE_DAILOG_ID);
			}
		});



		//set the start date to the current date
		final TextView startDateLabel = (TextView) findViewById(R.id.TextView_add_StartDate);
		CurrentDateValue.setToNow();
		startDateValue.setToNow();
		startDateFinalValue = startDateValue.toMillis(true);
		startDateLabel.setText(DateFormat.format("MMMM dd, yyyy", startDateFinalValue));

		//set the start time to the current date
		//        final TextView startTimeLabel = (TextView) findViewById(R.id.TextView_CreateInvite_StartTime_Label);
		//        startTimeLabel.setText("(pick start time)");

		final Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_add_Priority);
		spinner_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
					long selectedId) {

				ImageView img_icon = (ImageView)findViewById(R.id.image_add_Priority);
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



		Button btn_save = (Button)findViewById(R.id.btn_add_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
					if(!validate_Fields()){
						return;
					}

					add_item();
					Intent intent = new Intent(AddActivity.this, MainActivity.class);
					intent.putExtra("user_id", user_id);
					intent.putExtra("str_sortingFlag", str_sortingFlag);
					startActivity(intent);


				}
				catch(Exception e)
				{

				}


			}
		});

		Button btn_cancel = (Button)findViewById(R.id.btn_add_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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

	//
	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		getMenuInflater().inflate(R.menu.activity_main, menu);
	//		menu.findItem(R.id.menu_item_new).setIntent(new Intent(AddActivity.this, AddActivity.class));
	//		menu.findItem(R.id.menu_item_Hidden).setIntent(new Intent(AddActivity.this, HiddenItemsActivity.class));
	//		return true;
	//	}

	private void add_item()
	{
		EditText txt_item = (EditText)findViewById(R.id.EditText_add_task);
		EditText txt_desc = (EditText)findViewById(R.id.edittext_add_desc);
		Spinner spinner_priority = (Spinner)findViewById(R.id.spinner_add_Priority);

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

		datasource.createItem(str_Item, DueDate, str_Desc, str_priority, user_id, "1");

	}

	private DatePickerDialog.OnDateSetListener mDateSetListenerForStartDate = new DatePickerDialog.OnDateSetListener()
	{
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			try
			{
				final TextView startDateLabel = (TextView) findViewById(R.id.TextView_add_StartDate);

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

	private Boolean validate_Fields ()
	{
		EditText txt_task = (EditText)findViewById(R.id.EditText_add_task);

		String str_user = txt_task.getText().toString();

		if(str_user.equals(""))
		{
			showMessageToast(AddActivity.this, "Please enter task.!");
			return false;
		}

		int dateComparisonResult = Time.compare(startDateValue, CurrentDateValue);
		if(dateComparisonResult < 0) // date fields validation
		{
			showMessageToast(AddActivity.this,"You can not enter past date!");
			return false;
		}

		return true;
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {

		if (keyCode == KeyEvent.KEYCODE_BACK)  //Override Keyback to do nothing in this case.
		{
			try{
				EditText txt_task = (EditText)findViewById(R.id.EditText_add_task);
				String str_user = txt_task.getText().toString();

				if(str_user.equals(""))
				{

				}
				else
				{
					int dateComparisonResult = Time.compare(startDateValue,CurrentDateValue);
					if(dateComparisonResult < 0) // date fields validation
					{
						showMessageToast(AddActivity.this,"You can not enter past date!");
						return false;
					}
					add_item();
					Intent intent = new Intent(AddActivity.this, MainActivity.class);
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
}
