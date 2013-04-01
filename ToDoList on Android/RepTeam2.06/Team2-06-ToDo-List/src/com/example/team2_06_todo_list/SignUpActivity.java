package com.example.team2_06_todo_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.database.DataSource;
import com.example.database.UserItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

	private DataSource datasource;
	//private DBadapter dbadapter;
	private static ArrayList<HashMap<String, String>> UsersArrayList;
	private String USERS= "users";
	private String USER_id= "user_id";
	private String saved_user_id= null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		datasource = new DataSource(this);
		datasource.open();

		//	  dbadapter = new DBadapter(this);
		//    dbadapter.open();

		Button btn_signup = (Button)findViewById(R.id.btn_main_signup);
		btn_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validate_Fields())
				{
					Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
					intent.putExtra("user_id", saved_user_id);
					intent.putExtra("str_sortingFlag", "date");
					startActivity(intent);
					finish();
				}
				else
					return;

			}
		});

		Button btn_cancel = (Button)findViewById(R.id.btn_signup_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		//menu.findItem(R.id.menu_item_new).setIntent(new Intent(MainActivity.this, LoginActivity.class));
		return true;
	}

	private Boolean validate_Fields ()
	{
		EditText txt_userName = (EditText)findViewById(R.id.EditText_signup_usrname);
		EditText txt_pass = (EditText)findViewById(R.id.EditText_signup_pass);
		EditText txt_confirmPass = (EditText)findViewById(R.id.EditText_signup_pass_confirm);
		EditText txt_mail = (EditText)findViewById(R.id.EditText_signup_email);
		String str_usrName = txt_userName.getText().toString();
		String str_Pass = txt_pass.getText().toString();
		String str_confirmPass = txt_confirmPass.getText().toString();
		String str_email = txt_mail.getText().toString();

		if(str_usrName.equals(""))
		{
			showMessageToast(SignUpActivity.this, "Please enter username.");
			return false;
		}

		//checking if another user exeists or not
		get_users();
		int user_count = UsersArrayList.size();
		for(int i=0; i< user_count; i++)
		{
			String str_value = UsersArrayList.get(i).get(USERS);
			if(str_usrName.equals(str_value))
			{
				showAlert(SignUpActivity.this, "User alredy exists!", "Please choose a different username.");
				return false;
			}
		}

		if(str_Pass.equals(""))
		{
			showMessageToast(SignUpActivity.this, "Please enter password.");
			return false;
		}
		if(str_confirmPass.equals(""))
		{
			showMessageToast(SignUpActivity.this, "Please confirm password.");
			return false;
		}
		if(!(str_Pass.equals(str_confirmPass)))
		{
			showMessageToast(SignUpActivity.this, "The confirmed password did not match.");
			return false;
		}

		Pattern pattern = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher matcher = pattern.matcher(str_email);
		boolean matchFound = matcher.matches();
		if(matchFound == false)
		{
			showMessageToast(SignUpActivity.this,"Please enter a valid email");
			return false;
		}


		UserItem item = datasource.createUser(str_usrName, str_Pass, str_email);
		saved_user_id = String.valueOf(item.getId());
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

	public static void showAlert(Activity activityId,String title,String message) {

		try
		{
			AlertDialog alertDialog = new AlertDialog.Builder(activityId).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
				}   
			});
			alertDialog.show();
		}
		catch(Exception ex)
		{
			Log.v("Logged error : ", "showAlert() in MeetingWaveMainClass, userid");

		}
	}

	private void get_users()
	{
		try
		{
			UsersArrayList = new ArrayList<HashMap<String,String>>();
			DataSource mDbadapter = new DataSource(this);
			mDbadapter.open(); 
			Cursor data_cur = mDbadapter.fetchAllUsers();
			if(data_cur.getCount() >0)
			{ 
				if(data_cur.moveToFirst()) 
				{ 
					do   
					{
						HashMap<String,String> map = new HashMap<String, String>(); 

						String users = data_cur.getString(0);
						String user_id = data_cur.getString(1);
						map.put(USERS, users);
						map.put(USER_id, user_id);
						UsersArrayList.add(map);

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

}
