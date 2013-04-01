package com.example.cs6300todolist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.database.ToDoItem;
import com.example.database.ToDoItemDataSource;
import com.example.database.UserDataSource;
import com.example.database.Users;
import com.google.gson.Gson;

public class MainActivity extends Activity {

	private UserDataSource datasource;
	private ToDoItemDataSource itemdatasource;
	private ArrayList<Users> listofUsers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		datasource = new UserDataSource(this);
		datasource.open();
		listofUsers = new ArrayList<Users>();
		listofUsers.addAll(datasource.getAllUsers());

		/*    final Spinner user_spinner = (Spinner) findViewById(R.id.spinner1);
        List<Users> allusers = datasource.getAllUsers();
        ArrayAdapter<Users> adapter = new ArrayAdapter<Users>(this,
                android.R.layout.simple_spinner_item, allusers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.setAdapter(adapter); */
	}

	public void onAddClick(View view) {
		Intent myIntent = new Intent(view.getContext(), AddUserActivity.class);
		myIntent.putExtra("com.example.cs6300todolist.userid", 0);
		startActivityForResult(myIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		final EditText pwd_EditText = (EditText) findViewById(R.id.editText5);
		pwd_EditText.setText("");
		if (resultCode == RESULT_OK) {
			/*      final Spinner user_spinner = (Spinner) findViewById(R.id.spinner1);
            datasource.open();
            List<Users> allusers = datasource.getAllUsers();
            ArrayAdapter<Users> adapter = new ArrayAdapter<Users>(this,
                    android.R.layout.simple_spinner_item, allusers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            user_spinner.setAdapter(adapter); */
		}
	}

	public void onLoginClick(View view) throws JSONException, InterruptedException, ExecutionException {
		//  final Spinner user_spinner = (Spinner) findViewById(R.id.spinner1);
		final EditText username_EditText = (EditText) findViewById(R.id.editText_username);
		final EditText pwd_EditText = (EditText) findViewById(R.id.editText5);
		/*  if (user_spinner.getAdapter().getCount() == 0) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Login Error");
            alertDialog.setMessage("Please create user account.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        } */
		Users selectedUser = datasource.selectUser(username_EditText.getText().toString());
		Log.d("Ji", "username from edit text is "+username_EditText.getText().toString());

		// Users selectedUser = (Users) user_spinner.getSelectedItem();
		// if
		// (selectedUser.getName()!=null&&user_name_EditText.getText().length()>0)
		// {
		if (pwd_EditText.getText() != null
				&& pwd_EditText.getText().length() > 0) {
			String pwdStr = pwd_EditText.getText().toString().trim();
			Log.d("JI", "Before HTTP call ");
			Gson gson = new Gson();
			String uname = "?"+"name"+"="+username_EditText.getText().toString()+"&"+"encryptedPwd"+"="+pwdStr.toString();
			String json = gson.toJson(uname);
			Log.d("Ji","uname is "+uname);
			Communicator comm = new Communicator(json, "http://www.sdptaskstodo.appspot.com/api/user"+uname, "GET");                                                        
			String newresponse = null;
			try {
				newresponse = comm.execute().get();
				Log.d("After http call", "Output is "+ newresponse);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			Log.d("After http call newresponse", "Output is"+newresponse); 
			JSONObject loginresponse = new JSONObject(newresponse);
			String loginEncodedKey = loginresponse.getString("encodedKey");
			Log.d("loginencoded", "loginencodedkey is" +loginEncodedKey);

			Boolean check = false;
			if((newresponse.contains("Password Does")) || (newresponse.contains("User Does")))
			{
				check = false;
				Log.d("check", "setting check to false");
			}
			else
			{
				Log.d("check", "setting check to true");
				check = true;
				Users present = datasource.getUserByKey(loginEncodedKey);
				itemdatasource = new ToDoItemDataSource(this);
				itemdatasource.open();
				if(present == null)
				{
					datasource.createUsers(username_EditText.getText().toString(), PwdEncrypt.encrypt(pwdStr), loginEncodedKey);
					String presentMessage = "?username="+username_EditText.getText().toString();
					Communicator presentComm = new Communicator(presentMessage, "http://www.sdptaskstodo.appspot.com/api/task"+presentMessage, "GET");
					String presentResponse = presentComm.execute().get();
					if (presentResponse != null)
					{
						Log.d("Login Sync", "taks for user from server"+presentResponse);

						JSONArray presentArray = new JSONArray(presentResponse);
						for (Integer index = 0; index < presentArray.length(); index++)
						{
							JSONObject presentJson = presentArray.getJSONObject(index);

							String presentTaskname = presentJson.getString("name");
							String presentTaskKey = presentJson.getString("encodedKey");
							String presentTaskuserId = presentJson.getString("userId");
							String presentNote = presentJson.getString("note");
							long presentDueTime = presentJson.getLong("dueTime");
							boolean presentnoDueTime = presentJson.getBoolean("noDueTime");
							boolean presentChecked = presentJson.getBoolean("checked");
							long presentPriority = presentJson.getLong("priority");
							long presentCreation = presentJson.getLong("creationDate");

							
							Log.d("Pls", "to be added is "+ presentTaskuserId + " "+ presentTaskname + " "+presentNote +
									" "+presentDueTime + " "+presentnoDueTime + " "+presentChecked +
									" "+presentPriority + " "+presentTaskKey + " "+presentCreation);
							ToDoItem presentaddeditem = itemdatasource.createItem(presentTaskuserId, presentTaskname, presentNote,
									presentDueTime, presentnoDueTime, presentChecked,
									presentPriority, presentTaskKey.toString(), presentCreation);
						}
					}
				}
			}

			// if (PwdEncrypt.encrypt(pwdStr).equals(selectedUser.getPwd())) {
			if(check) {
				// Login view;
				Intent myIntent = new Intent(view.getContext(),
						ListsActivity.class);
				//myIntent.putExtra("com.example.cs6300todolist.userid",
				//  selectedUser.getId());
				myIntent.putExtra("com.example.cs6300todolist.userkey",
						loginEncodedKey);
				myIntent.putExtra("com.example.cs6300todolist.username",
						username_EditText.getText().toString());
				startActivityForResult(myIntent, 0);
			} else {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Login Error");
				alertDialog.setMessage("Password do not match.");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
						new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
				alertDialog.show();
			}
		} else {
			AlertDialog alertDialog;
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Login Error");
			alertDialog.setMessage("Password field cannot be empty.");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {


				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}