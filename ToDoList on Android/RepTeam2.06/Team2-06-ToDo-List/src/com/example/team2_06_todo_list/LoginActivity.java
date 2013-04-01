package com.example.team2_06_todo_list;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.database.DataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LoginActivity extends Activity {

	private DataSource datasource;
	private static ArrayList<HashMap<String, String>> UsersArrayList;
	private String USERNAME= "username";
	private String USER_id= "user_id";
	private String PASSWORD= "password";
	private static String saved_user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		datasource = new DataSource(this);
		datasource.open();

		Button btn_login = (Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
					if(!validate_Fields()){
						return;
					}
					else
					{
						if(!(saved_user_id.equals(null))|| !(saved_user_id.equals(""))){
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							intent.putExtra("user_id", saved_user_id);
							intent.putExtra("str_sortingFlag", "date");
							startActivity(intent); 
						}
					}
				}
				catch(Exception e)
				{

				}


			}
		});

		Button btn_signup = (Button)findViewById(R.id.btn_Signup);
		btn_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent); 
			}
		});
	}

	private Boolean validate_Fields ()
	{
		EditText txt_userName = (EditText)findViewById(R.id.EditText_username);
		EditText txt_pass = (EditText)findViewById(R.id.EditText_Password);

		String str_user = txt_userName.getText().toString();
		String str_pass = txt_pass.getText().toString();

		if(str_user.equals(""))
		{
			showMessageToast(LoginActivity.this, "Please enter Username");
			return false;
		}
		if(str_pass.equals(""))
		{
			showMessageToast(LoginActivity.this, "Please enter Password");
			return false;
		}

		//checking if another user exists or not
		get_users();
		int user_count = UsersArrayList.size();
		if(user_count == 0)
		{
			showMessageToast(LoginActivity.this, "Not a user!");
			return false;
		}
		else
		{
			for(int i=0; i< user_count; i++)
			{
				String str_name = UsersArrayList.get(i).get(USERNAME);
				String str_password = UsersArrayList.get(i).get(PASSWORD);
				if(str_user.equals(str_name) && str_pass.equals(str_password))
				{
					saved_user_id = UsersArrayList.get(i).get(USER_id);
					return true;
				}
			}
			showAlert(LoginActivity.this, "Invalid credentials!", "Either the username or password did not match.");
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
			Cursor data_cur = mDbadapter.getUserInfo();
			if(data_cur.getCount() >0)
			{ 
				if(data_cur.moveToFirst()) 
				{ 
					do   
					{
						HashMap<String,String> map = new HashMap<String, String>(); 

						String user_id = data_cur.getString(0);
						String username = data_cur.getString(1);
						String pass = data_cur.getString(2);
						map.put(USER_id, user_id);
						map.put(USERNAME, username);
						map.put(PASSWORD, pass);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (datasource != null) {
			datasource.close();
		}
	
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if (datasource != null) {
			datasource.open();
		}

     // get the instances of the username and password field 
  	       EditText txtUsername = (EditText)findViewById(R.id.EditText_username);
  	       EditText txtPassword = (EditText)findViewById(R.id.EditText_Password);
  	       
  	       txtUsername.setText("");
  	       txtPassword.setText("");
  	       
        
    }

}
