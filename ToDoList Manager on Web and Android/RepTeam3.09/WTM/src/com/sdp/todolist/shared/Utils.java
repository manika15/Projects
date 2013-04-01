package com.sdp.todolist.shared;

import com.google.gson.Gson;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class Utils {
	private static final String API_URL = "http://" + Window.Location.getHost() + "/api/";
	
	public static String getQueryURL(String servlet, String query) {
		return API_URL + servlet + ((query != null) ? "?" + query : "");
	}
	
	public static XMLHttpRequest query(String method, String servlet, String query, ReadyStateChangeReceiver handler) {
		XMLHttpRequest xml = XMLHttpRequest.create();
		xml.setOnReadyStateChange(handler);
		if (method.equals("POST")) {
			xml.open(method, Utils.getQueryURL(servlet, null));
			xml.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			xml.setRequestHeader("Content-length", String.valueOf(query.length()));
			xml.setRequestHeader("Connection", "close");
			xml.send(query);
		}
		else {
			xml.open(method, Utils.getQueryURL(servlet, query));
			xml.send();
		}
		
		return xml;
	}
	
	public static XMLHttpRequest query(String method, String servlet, Object obj, ReadyStateChangeReceiver handler) {
		XMLHttpRequest xml = XMLHttpRequest.create();
		xml.setOnReadyStateChange(handler);
		
		String json = obj.toString();
		
		xml.open(method, Utils.getQueryURL(servlet, null));
		xml.setRequestHeader("Content-type", "application/json");
		xml.setRequestHeader("Content-length", String.valueOf(json.length()));
		xml.setRequestHeader("Connection", "close");
		xml.send(json);
		
		return xml;
	}
	
	public static abstract class ReadyStateChangeReceiver implements ReadyStateChangeHandler {
		@Override
		public void onReadyStateChange(XMLHttpRequest xhr) {
			if (xhr.getReadyState() == XMLHttpRequest.DONE) {
				if (xhr.getStatus() != 200) {
					error(xhr.getStatusText());
				}
				else {
					succeeded(xhr.getResponseText());
				}
			}
		}
		
		protected abstract void error(String msg);
		protected abstract void succeeded(String msg);
	}
}
