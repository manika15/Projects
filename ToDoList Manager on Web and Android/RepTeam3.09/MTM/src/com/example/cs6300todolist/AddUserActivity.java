package com.example.cs6300todolist;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.UserDataSource;
import com.example.database.Users;
import com.google.gson.Gson;

public class AddUserActivity extends Activity {

    private UserDataSource datasource;
    private long userId = 0;
    private String userKey = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        datasource = new UserDataSource(this);
        datasource.open();
       // userId = getIntent().getLongExtra("com.example.cs6300todolist.userid",
         //       0);
        userKey = getIntent().getStringExtra("com.example.cs6300todolist.userkey");
        if (userKey == null) {
            this.setTitle("Add New User");
        } else {
            this.setTitle("Change Password");
            final EditText user_name_EditText = (EditText) findViewById(R.id.editText1);
            Users user = datasource.getUserById(userId);
            user_name_EditText.setText(user.getName());
            user_name_EditText.setEnabled(false);
            final Button addButton = (Button) findViewById(R.id.button1);
            addButton.setText(R.string.changepwd);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_user, menu);
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

    public void onAddUserClick(View view) throws JSONException {
        final EditText user_name_EditText = (EditText) findViewById(R.id.editText1);
        final EditText pwd_EditText = (EditText) findViewById(R.id.editText2);
        final EditText confirm_pwd_EditText = (EditText) findViewById(R.id.editText3);
        if (user_name_EditText.getText() != null
                && user_name_EditText.getText().length() > 0) {
            if (pwd_EditText.getText() != null
                    && confirm_pwd_EditText.getText() != null
                    && pwd_EditText.getText().length() > 0
                    && confirm_pwd_EditText.getText().length() > 0) {
                String pwdStr = pwd_EditText.getText().toString().trim();
                String confirm_pwdStr = confirm_pwd_EditText.getText()
                        .toString().trim();
                String userName = user_name_EditText.getText().toString()
                        .trim();
                if (pwdStr.equals(confirm_pwdStr)) {
                    if (userKey == null) {
                        if (datasource.selectUser(userName) == null) {                        	                           
                        	 JSONObject jsonobject = new JSONObject();
                        	 try {
								jsonobject.put("name", userName);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                        	 try {
 								jsonobject.put("encryptedPwd", pwdStr);
 							} catch (JSONException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
                           
                            
                       // 	String namepost="?"+"name="+userName+"&"+"encryptedPwd="+pwdStr;
                       //   Log.d("Ji","post query is" +namepost);
                        	 Log.d("ADD user", "message is "+jsonobject.toString());
                            Communicator comm = new Communicator(jsonobject.toString(), "http://www.sdptaskstodo.appspot.com/api/user", "POST");
                        //    Communicator comm = new Communicator(namepost, "http://www.sdptaskstodo.appspot.com/api/user"+namepost, "POST");
                            
                            String response = null;
							try {
								response = comm.execute().get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            
                            Log.d("JI", "Output from server is "+response);
                            JSONObject outputobject = null;
                            String encodedKey = null;
							try {
								outputobject = new JSONObject(response);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            try {
								 encodedKey = outputobject.getString("encodedKey");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            
                            Log.d("encodedKey", "encodedKey is" +encodedKey);
                            
                            if (datasource.createUsers(userName,
                                    PwdEncrypt.encrypt(pwdStr), encodedKey) != null) {
                                Toast toast = Toast.makeText(this,
                                        "User created.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            AlertDialog alertDialog;
                            alertDialog = new AlertDialog.Builder(this)
                                    .create();
                            alertDialog.setTitle("Create User Account Error");
                            alertDialog.setMessage("User name exists.");
                            alertDialog.setButton(
                                    DialogInterface.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    } else {
                        Users updateUser = new Users();
                        updateUser.setId(userId);
                        updateUser.setName(userName);
                        updateUser.setPwd(PwdEncrypt.encrypt(pwdStr));
                        JSONObject userUpdateObject = new JSONObject();
                        Users chuser = datasource.getUserByKey(userKey);
                      
                        // String chencodedkey = chuser.getEncodedKey();
                        //Log.d("update", "encoded key obatained is "+chuser.getEncodedKey());
                        
                        userUpdateObject.put("encodedKey", userKey);
                        userUpdateObject.put("name", userName);
                        userUpdateObject.put("encryptedPwd", pwdStr);
                        updateUser.setEncodedKey(userKey);
                   
                        Communicator commupdate = new Communicator(userUpdateObject.toString(), "http://www.sdptaskstodo.appspot.com/api/user", "PUT");
                        String updateResponse = null;
                        
                        try {
							updateResponse = commupdate.execute().get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        Log.d("Update", "Response is "+updateResponse);
                        
                        if (datasource.updateUsers(updateUser) > 0) {
                            Toast toast = Toast.makeText(this,
                                    "Password changed.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create User Account Error");
                    alertDialog.setMessage("Passwords don't match.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

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
                alertDialog.setTitle("Create User Account Error");
                alertDialog.setMessage("Password cannot be empty.");
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
            alertDialog.setTitle("Create User Account Error");
            alertDialog.setMessage("User name cannot be empty.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }
}
