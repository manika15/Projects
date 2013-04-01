package com.example.cs6300todolist;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import android.util.Log;

public class Communicator extends AsyncTask<String, String, String>{
	
	private String message;
	private String url;
	private String method;
	private String response;
	
	public Communicator(String message, String url, String method){
		this.message = message;
		this.url = url;
		this.method = method;
	}


	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String response = null;
		if(method.equals("POST")){			
			Log.d("Ji", "Inside communicator POST");
            HttpPost httpPost = new HttpPost(url);
            try {
            	Log.d("Ji", "Within httppost url " +message);
            
				httpPost.setEntity(new StringEntity(message, "UTF8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};

            HttpResponse httpResponse;
			try {
				Log.d("Ji","within httpresponse");
				httpResponse = httpClient.execute(httpPost);
				InputStream inputStream = httpResponse.getEntity().getContent();
				response = convertStreamToString(inputStream);
				Log.d("Ji","after response " +response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		if(method.equals("GET")){
			HttpGet httpGet = new HttpGet(url);
            
            Log.d("GET", "url is "+url);
            HttpResponse httpResponse;
			try {
				Log.d("Ji", "before execute");
				httpResponse = httpClient.execute(httpGet);	            	            
	            InputStream inputStream = httpResponse.getEntity().getContent();
	            response = convertStreamToString(inputStream);
	            Log.d("GET JI", "response is "+response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(method.equals("PUT")){			
			Log.d("Ji", "Inside communicator PUT");
            HttpPut httpPut = new HttpPut(url);
            try {
            	Log.d("Ji", "Within httpput url " +message);
            
				httpPut.setEntity(new StringEntity(message, "UTF8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};

            HttpResponse httpResponse;
			try {
				Log.d("Ji","within httpresponse");
				httpResponse = httpClient.execute(httpPut);
				InputStream inputStream = httpResponse.getEntity().getContent();
				response = convertStreamToString(inputStream);
				Log.d("Ji","after response " +response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		if(method.equals("DELETE")){			
			Log.d("Ji", "Inside communicator DELETE");
            HttpDelete httpDelete = new HttpDelete(url);
            try {
            	Log.d("Ji", "Within httpdelete url " +message);
            
				((HttpResponse) httpDelete).setEntity(new StringEntity(message, "UTF8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};

            HttpResponse httpResponse;
			try {
				Log.d("Ji","within httpresponse");
				httpResponse = httpClient.execute(httpDelete);
				InputStream inputStream = httpResponse.getEntity().getContent();
				response = convertStreamToString(inputStream);
				Log.d("Ji","after response " +response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		
		return response;
	}

	private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {        
            return "";
        }
     }
	
	 @Override
	    protected void onPostExecute(String json ) {
		 	Log.d("JI", "Returning data from Async task " + json);
		 	getResult();
	    }

        public String getResult (){
        	return response;
        }

}
