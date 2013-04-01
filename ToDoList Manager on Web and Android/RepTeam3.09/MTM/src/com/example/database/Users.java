package com.example.database;

public class Users {
	private long id;
	private String name;
	private String encryptedPwd;
	private String encodedKey;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPwd() {
		return encryptedPwd;
	}

	public void setPwd(String pwd) {
		this.encryptedPwd = pwd;
	}


	@Override
	public String toString() {
		return name;
	}
} 
