package com.example.database;

public class ToDoItems {

	private long id;
	private String ToDoItem;
	private String DudeDate;
	private String Description;
	private String Priority;
	private String Status;
	private long Reference_id;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPriority() {
		return Priority;
	}

	public void setPriority(String priority) {
		Priority = priority;
	}

	public long getReference_id() {
		return Reference_id;
	}

	public void setReference_id(long reference_id) {
		Reference_id = reference_id;
	}

	public String getDudeDate() {
		return DudeDate;
	}

	public void setDudeDate(String dudeDate) {
		DudeDate = dudeDate;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToDoItem() {
		return ToDoItem;
	}

	public void setToDoItem(String item) {
		this.ToDoItem = item;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return ToDoItem;
	}
}
