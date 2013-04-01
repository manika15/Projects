package com.sdp.todolist.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

@PersistenceCapable()
public class User {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String encodedKey;
	private String name;
	private String encryptedPwd;
	
	public String toString() {
		String s;
		if (encodedKey == null) {
			s = "{";
		}
		else {
			s = "{\"encodedKey\":\"" + encodedKey + "\", ";
		}
		s = s + "\"name\":\"" + name + "\", \"encryptedPwd\":\"" + encryptedPwd+ "\"}";

		return s;
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
	public String getEncryptedPwd() {
		return encryptedPwd;
	}
	public void setEncryptedPwd(String encryptedPwd) {
		this.encryptedPwd = encryptedPwd;
	}
}
