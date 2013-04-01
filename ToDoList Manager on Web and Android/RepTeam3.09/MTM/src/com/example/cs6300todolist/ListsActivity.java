package com.example.cs6300todolist;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class ListsActivity extends TabActivity{
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_list);
	        TabHost tabHost = getTabHost();
	        
	        //final long userId = getIntent().getLongExtra("com.example.cs6300todolist.userid", 0);
	        final String userKey = getIntent().getStringExtra("com.example.cs6300todolist.userkey");
	        Log.d("JI", "userKey is "+userKey);
	        final String userName = getIntent().getStringExtra("com.example.cs6300todolist.username");
	                
	        
	        // All tasks
	        TabSpec allTasks = tabHost.newTabSpec("All");
	        allTasks.setIndicator("All");
	        Intent allTasksIntent = new Intent(this, AllTasksActivity.class);
	        //allTasksIntent.putExtra("com.example.cs6300todolist.userid", userId);
	        allTasksIntent.putExtra("com.example.cs6300todolist.userkey", userKey);
	        allTasksIntent.putExtra("com.example.cs6300todolist.username", userName);
	        allTasks.setContent(allTasksIntent);
	        allTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	        // Pending tasks
	        TabSpec pendingTasks = tabHost.newTabSpec("Pending");
	        pendingTasks.setIndicator("Pending");
	        Intent pendingTasksIntent = new Intent(this, PendingTasksActivity.class);
	        //pendingTasksIntent.putExtra("com.example.cs6300todolist.userid", userId);
	        pendingTasksIntent.putExtra("com.example.cs6300todolist.userkey", userKey);
	        pendingTasksIntent.putExtra("com.example.cs6300todolist.username", userName);
	        pendingTasks.setContent(pendingTasksIntent);
	        pendingTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        // Completed tasks
	        TabSpec  completedTasks = tabHost.newTabSpec("Completed");
	        completedTasks.setIndicator("Completed");
	        Intent completedTasksIntent = new Intent(this, CompletedTasksActivity.class);
	        //completedTasksIntent.putExtra("com.example.cs6300todolist.userid", userId);
	        completedTasksIntent.putExtra("com.example.cs6300todolist.userkey", userKey);
	        completedTasksIntent.putExtra("com.example.cs6300todolist.username", userName);
	        completedTasks.setContent(completedTasksIntent);
	        completedTasksIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        
	        Log.d("JI", "Inside Lists Activity -->>");
	        tabHost.addTab(allTasks); 
	        tabHost.addTab(pendingTasks);
	        tabHost.addTab(completedTasks); 
	        final int tab_index = getIntent().getExtras().getInt("tab_index");	
	        Log.d("JI", "tab_index is :" + tab_index);
	        tabHost.setCurrentTab(tab_index);
	    }
	

}
