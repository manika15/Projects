package com.example.database;

public class UserItem {

	private long id;
	private String UserItem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserItem() {
		return UserItem;
	}

	public void setUserItem(String userItem) {
		UserItem = userItem;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return UserItem;
	}
}
