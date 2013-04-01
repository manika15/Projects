package com.sdp.todolist.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.FocusPanel;

@PersistenceCapable
public class Task {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String encodedKey;	
	private String userId;
	
	private String name;	
	private String note;
	private long dueTime;
	private boolean checked; 
	private boolean noDueTime;
	private long priority;
	private long creationDate;
	private boolean deleted;
	
	public boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String toString() {
		String s;
		if (encodedKey == null) {
			s = "{";
		}
		else {
			s = "{\"encodedKey\":\"" + encodedKey + "\", ";
		}
		s = s + "\"deleted\":\"" + deleted + "\", \"userId\":\"" + userId + "\", \"name\":\"" + name + "\", \"note\":\"" + note + "\", \"dueTime\":\"" + dueTime + "\", \"checked\":\"" + checked + "\", \"noDueTime\":\"" + noDueTime + "\", \"priority\":\"" + priority + "\", \"creationDate\":\"" + creationDate + "\"}";
		
		return s; 
	}
	
	private transient FocusPanel panel;

	public void setPanel(FocusPanel p) {
		panel = p;
	}
	
	public FocusPanel getPanel() {
		return panel;
	}
	
	public String getEncodedKey() {
		return encodedKey;
	}
	public void setEncodedKey(String encodedKey) {
		this.encodedKey = encodedKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getDueTime() {
		return dueTime;
	}
	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isNoDueTime() {
		return noDueTime;
	}
	public void setNoDueTime(boolean noDueTime) {
		this.noDueTime = noDueTime;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

		
}