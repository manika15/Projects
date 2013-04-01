package com.example.team2_06_todo_list;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataSource;
public class HiddenItemsActivity extends Activity {
	private DataSource datasource;


	String user_id;
	String item_id;
	
	public static final int SetSortDate = 0;
	public static final int SetPrioritySort = 1;
	public static final int Delete = 2;
	public static final int Edit = 3;


	static final String ITEM_id = "item_id";
	static final String ITEM = "item";
	static final String DUE_DATE = "due_date";     
	static final String DESCRIPTION = "description";
	static final	String PRIOITY = "priority";
	static final String STATUS = "status";
	static final String REFRENCE_ID = "reference_id";

	private static ArrayList<HashMap<String, String>> ToDoArrayList = new ArrayList<HashMap<String,String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hidden_items);

		datasource = new DataSource(this);
		datasource.open();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		user_id = extras.getString("user_id");
		item_id = extras.getString("item_id");

		fillData();

		ListView lst_todo = (ListView)findViewById(R.id.hidden_listView);
		EfficientAdapter adapter = new EfficientAdapter(this);
		lst_todo.setAdapter(adapter);
		lst_todo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				item_id = ToDoArrayList.get(pos).get(ITEM_id);
				registerForContextMenu(v); 
				openContextMenu(v);			}
		});

	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case SetSortDate:
//			fillData();
//			ListView lst_todo_date = (ListView)findViewById(R.id.main_listView);
//			EfficientAdapter adapter_date = new EfficientAdapter(this);
//			lst_todo_date.setAdapter(adapter_date);
//			break;
//		case SetPrioritySort:
//			fillData();
//			ListView lst_todo = (ListView)findViewById(R.id.main_listView);
//			EfficientAdapter adapter = new EfficientAdapter(this);
//			lst_todo.setAdapter(adapter);
//			break;
		case Delete:
			final AlertDialog Hide_dialog = new AlertDialog.Builder(this).create();
			Hide_dialog.setTitle("Confirm Delete");
			Hide_dialog.setMessage("Are you sure you want to delete?");

			Hide_dialog.setButton("Yes", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					delete_items();
				}
			});

			Hide_dialog.setButton2("No", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Hide_dialog.cancel();
				}
			});

			Hide_dialog.show();


			break;
//		case Edit:
//			Intent intent = new Intent(HiddenItemsActivity.this, EditActivity.class);
//			intent.putExtra("user_id", user_id);
//			intent.putExtra("item_id", item_id);
//			startActivity(intent);
//			break;
//		case Details:
//			Intent intent_details = new Intent(MainActivity.this, DetailsActivity.class);
//			intent_details.putExtra("user_id", user_id);
//			intent_details.putExtra("item_id", str_id);
//			startActivity(intent_details);
//			break;


		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Menu");
//		menu.add(0, SetSortDate, 0, "Sort list by date");
//		menu.add(0, SetPrioritySort, 1, "Sort list by priority");
		menu.add(0, Delete, 2, "Delete");
//		menu.add(0, Edit, 3, "Edit");
//		menu.add(0, Details, 3, "Details");
	}
	

	//    @Override
	//    public boolean onCreateOptionsMenu(Menu menu) {
	//		getMenuInflater().inflate(R.menu.activity_main, menu);
	//		menu.findItem(R.id.menu_item_new).setIntent(new Intent(HiddenItemsActivity.this, AddActivity.class));
	//		menu.findItem(R.id.menu_item_Hidden).setIntent(new Intent(HiddenItemsActivity.this, HiddenItemsActivity.class));
	//        return true;
	//    }

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return ToDoArrayList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_listview, null);
				holder = new ViewHolder();

				holder.txtTodoItem = (TextView) convertView.findViewById(R.id.txt_list_todo);
				holder.txtDueDate = (TextView) convertView.findViewById(R.id.txt_list_due_date);
				holder.imgPriority = (ImageView) convertView.findViewById(R.id.txt_list_priority);
				holder.chk_hide = (CheckBox) convertView.findViewById(R.id.lst_chk_box);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}



			if(ToDoArrayList.get(position).get(STATUS).equals("0"))
			{
				holder.chk_hide.setChecked(false);
				String str_Item = ToDoArrayList.get(position).get(ITEM);
				if(str_Item.length() > 15)
				{
					str_Item = str_Item.substring(0,15) + "...";
				}
				holder.txtTodoItem.setText(str_Item);
				holder.txtDueDate.setText(ToDoArrayList.get(position).get(DUE_DATE));
				//holder.txtPriority.setText(ToDoArrayList.get(position).get(PRIOITY));
				String str_priority = ToDoArrayList.get(position).get(PRIOITY);

				if(str_priority.equals("1"))
				{
					holder.imgPriority.setImageResource(R.drawable.red);
				}
				else if(str_priority.equals("2"))
				{
					holder.imgPriority.setImageResource(R.drawable.blue);
				}
				else if(str_priority.equals("3"))
				{
					holder.imgPriority.setImageResource(R.drawable.yellow);
				}
			}

			holder.chk_hide.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					try
					{
						final AlertDialog Hide_dialog = new AlertDialog.Builder(v.getContext()).create();
						Hide_dialog.setTitle("Confirm Unhide");
						Hide_dialog.setMessage("Are you sure you want to unhide this item?");

						Hide_dialog.setButton("Yes", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String id = ToDoArrayList.get(position).get(ITEM_id);
								((HiddenItemsActivity) v.getContext()).datasource.UpdateItem(id, null, null, null, null, "1");
								((HiddenItemsActivity) v.getContext()).fillData();
								//ToDoArrayList.remove(position);
								notifyDataSetChanged();

							}
						});

						Hide_dialog.setButton2("No", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								holder.chk_hide.setChecked(false);
								Hide_dialog.cancel();
							}
						});

						Hide_dialog.show();


					}
					catch(Exception ex)
					{
						Log.v("Logged error : ", "holder.txtHideRow.setOnClickListener() in NearByActivity, userid");
					}
				}
			});

			//i++;
			return convertView; 


		}

		static class ViewHolder {
			TextView txtTodoItem;
			TextView txtDueDate;
			ImageView imgPriority;
			CheckBox chk_hide;
		}
	}

	private void fillData() {

		try
		{
			ToDoArrayList = new ArrayList<HashMap<String,String>>();
			DataSource mDbadapter = new DataSource(this);
			mDbadapter.open(); 
			Cursor data_cur = mDbadapter.fetchAllHiddenItems(user_id);
			if(data_cur.getCount() >0)
			{ 
				if(data_cur.moveToFirst()) 
				{ 
					do   
					{
						HashMap<String,String> map = new HashMap<String, String>(); 

						String item_id = data_cur.getString(0);
						String item = data_cur.getString(1);
						String due_date = data_cur.getString(2);     
						String description = data_cur.getString(3);
						String priority = data_cur.getString(4);
						String status = data_cur.getString(5);

						map.put(ITEM_id, item_id);
						map.put(ITEM, item);
						map.put(DUE_DATE, due_date);
						map.put(DESCRIPTION, description);
						map.put(PRIOITY, priority);
						map.put(STATUS, status);
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
			// TODO: handle exception\
		}

	}
	
	private void delete_items()
	{
//		datasource.open();
		datasource.deleteItem(item_id);
		ListView lst_todo = (ListView)findViewById(R.id.hidden_listView);
		fillData();
		EfficientAdapter adapter = new EfficientAdapter(this);
		lst_todo.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (datasource != null) {
			datasource.close();
		}

	}



}
