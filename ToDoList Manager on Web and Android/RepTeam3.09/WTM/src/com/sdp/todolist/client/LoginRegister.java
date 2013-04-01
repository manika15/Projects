package com.sdp.todolist.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.sdp.todolist.shared.User;
import com.sdp.todolist.shared.Utils;

public class LoginRegister implements EntryPoint {

	private Button sendLogin;
	private TextBox usernameField;
	private PasswordTextBox passwordField;
	private PasswordTextBox password2Field;
	private boolean usernameOK;
	private boolean passwordOK;
	private boolean passwordRepeatOK;
	
	private boolean newaccount = false;
	
	public void onModuleLoad() {
		sendLogin = new Button("Sign in");
		final Button sendRegister = new Button("Register");
		usernameField = new TextBox();
		passwordField = new PasswordTextBox();
		password2Field = new PasswordTextBox();
		
		sendLogin.addStyleName("btn");
		sendLogin.addStyleName("btn-large");
		sendLogin.addStyleName("btn-primary");
		
		sendRegister.addStyleName("btn");
		sendRegister.addStyleName("btn-small");
		
		usernameField.getElement().setAttribute("placeHolder", "Username");
		passwordField.getElement().setAttribute("placeHolder", "Password");
		password2Field.getElement().setAttribute("placeHolder", "Repeat Password");
		
		password2Field.addStyleName("hidden");
		
		RootPanel.get("registerfld").add(sendRegister);
		RootPanel.get("usernameinp").add(usernameField);
		RootPanel.get("passwordinp").add(passwordField);
		RootPanel.get("password2inp").add(password2Field);
		RootPanel.get("sendlogin").add(sendLogin);
		
		usernameField.setFocus(true);
		usernameField.selectAll();
		
		sendRegister.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				usernameField.setValue("");
				passwordField.setValue("");
				password2Field.setValue("");
				
				RootPanel.get("passwordwrapper").removeStyleName("error");
				RootPanel.get("passwordwrapper").removeStyleName("success");
				RootPanel.get("passwordwrapper").removeStyleName("info");
				
				RootPanel.get("password2wrapper").removeStyleName("error");
				RootPanel.get("password2wrapper").removeStyleName("success");
				RootPanel.get("password2wrapper").removeStyleName("info");
				
				RootPanel.get("usernamewrapper").removeStyleName("error");
				RootPanel.get("usernamewrapper").removeStyleName("success");
				RootPanel.get("usernamewrapper").removeStyleName("info");
				
				usernameOK = false;
				passwordOK = false;
				passwordRepeatOK = false;
				
				preventLogin(true);
				
				usernameField.setFocus(true);
				usernameField.selectAll();
				
				newaccount = !newaccount;
				
				if (newaccount) {
					password2Field.removeStyleName("hidden");
					sendLogin.addStyleName("btn-large-comp");
					sendRegister.setText("Login");
					sendLogin.setText("Register Account");
				}
				else {
					password2Field.addStyleName("hidden");
					sendLogin.removeStyleName("btn-large-comp");
					sendRegister.setText("Register");
					sendLogin.setText("Sign in");
				}
			}
		});
		
		sendLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginOrRegister(usernameField.getText());
			}			
		});
		
		usernameOK = false;
		passwordOK = false;
		passwordRepeatOK = false;
		
		class UsernameHandler implements BlurHandler, KeyUpHandler {
			boolean ignore = false;
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (ignore) {
					ignore = false;
					return;
				}
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					ignore = true;
					loginOrRegister(usernameField.getText());
				}
				else {
					checkUsername(false);
				}
			}

			@Override
			public void onBlur(BlurEvent event) {
				checkUsername(true);
			}
			
			private void checkUsername(boolean length) {
				String username = usernameField.getValue();
				
				if ((!length && username.length() > 3) || (length && username.length() > 0)) {
					
					Utils.query("GET", "user", "name=" + username + "&encryptedPwd=%20", new Utils.ReadyStateChangeReceiver() {
						@Override
						protected void error(String msg) {
							RootPanel.get("usernamewrapper").removeStyleName("error");
							RootPanel.get("usernamewrapper").removeStyleName("success");
							RootPanel.get("usernamewrapper").removeStyleName("info");
							
							RootPanel.get("usernamewrapper").addStyleName("error");
							
							preventLogin(true);
							usernameOK = false;
							sendLogin.setText("Fix username problem");
						}

						@Override
						protected void succeeded(String msg) {
							RootPanel.get("usernamewrapper").removeStyleName("error");
							RootPanel.get("usernamewrapper").removeStyleName("success");
							RootPanel.get("usernamewrapper").removeStyleName("info");
							
							JSONValue v = JSONParser.parseStrict(msg);
							if (v.isString() != null && v.isString().toString().equals("\"User Does Not Exist!\"")) {
								if (newaccount) {
									usernameOK = true;
									sendLogin.setText("Register account");
									RootPanel.get("usernamewrapper").addStyleName("success");
								}
								else {
									usernameOK = false;
									sendLogin.setText("Username doesn't exist");
									RootPanel.get("usernamewrapper").addStyleName("error");
								}
							}
							else {
								if (newaccount) {
									usernameOK = false;
									sendLogin.setText("Username already exist");
									RootPanel.get("usernamewrapper").addStyleName("error");
								}
								else {
									usernameOK = true;
									sendLogin.setText("Sign in");
									newaccount = false;
									RootPanel.get("usernamewrapper").addStyleName("info");
								}
							}
							
							if (passwordOK && (!newaccount || passwordRepeatOK)) {
								preventLogin(false);
							}
						}
					});
					
					
				}
				else {
					RootPanel.get("usernamewrapper").removeStyleName("error");
					RootPanel.get("usernamewrapper").removeStyleName("success");
					RootPanel.get("usernamewrapper").removeStyleName("info");
					
					usernameOK = false;
					preventLogin(true);
					sendLogin.setText("Sign in");
				}
			}
		}
		
		class PasswordHandler implements BlurHandler, KeyUpHandler {
			boolean ignore = false;
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (ignore) {
					ignore = false;
					return;
				}
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					ignore = true;
					loginOrRegister(usernameField.getText());
				}
				else {
					checkPassword(false);
				}
			}

			@Override
			public void onBlur(BlurEvent event) {
				checkPassword(true);
			}
			
			private void checkPassword(boolean length) {
				String password = passwordField.getValue();
				
				if ((!length && password.length() > 5) || (length && password.length() > 0)) {
					if (password.length() > 5) {
						RootPanel.get("passwordwrapper").removeStyleName("error");
						RootPanel.get("passwordwrapper").addStyleName("info");
						
						passwordOK = true;
						
						if (usernameOK && (!newaccount || passwordRepeatOK)) {
							preventLogin(false);
						}
					}
					else {
						RootPanel.get("passwordwrapper").removeStyleName("info");
						RootPanel.get("passwordwrapper").addStyleName("error");
						
						passwordOK = false;
					}
				}
				else {
					RootPanel.get("passwordwrapper").removeStyleName("error");
					RootPanel.get("passwordwrapper").removeStyleName("info");
					
					preventLogin(true);
					passwordOK = false;
				}
			}
		}
		
		class Password2Handler implements BlurHandler, KeyUpHandler {
			boolean ignore = false;
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (ignore) {
					ignore = false;
					return;
				}
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					ignore = true;
					loginOrRegister(usernameField.getText());
				}
				else {
					checkPassword(false);
				}
			}

			@Override
			public void onBlur(BlurEvent event) {
				checkPassword(true);
			}
			
			private void checkPassword(boolean length) {
				String password = password2Field.getValue();
				
				if ((!length && password.length() > 5) || (length && password.length() > 0)) {
					if (password.length() > 5) {
						RootPanel.get("password2wrapper").removeStyleName("error");
						RootPanel.get("password2wrapper").addStyleName("info");
						
						passwordRepeatOK = true;
						
						if (usernameOK && passwordOK) {
							preventLogin(false);
						}
					}
					else {
						RootPanel.get("password2wrapper").removeStyleName("info");
						RootPanel.get("password2wrapper").addStyleName("error");
						
						passwordRepeatOK = false;
					}
				}
				else {
					RootPanel.get("password2wrapper").removeStyleName("error");
					RootPanel.get("password2wrapper").removeStyleName("info");
					
					preventLogin(true);
					passwordRepeatOK = false;
				}
			}
		}
		
		UsernameHandler userhandler = new UsernameHandler();
		usernameField.addBlurHandler(userhandler);
		usernameField.addKeyUpHandler(userhandler);
		
		PasswordHandler passhandler = new PasswordHandler();
		passwordField.addBlurHandler(passhandler);
		passwordField.addKeyUpHandler(passhandler);
		
		Password2Handler pass2handler = new Password2Handler();
		password2Field.addBlurHandler(pass2handler);
		password2Field.addKeyUpHandler(pass2handler);
		
		preventLogin(true);
	}
	
	private void preventLogin(boolean prevent) {
		if (prevent) {
			sendLogin.addStyleName("disabled");
			sendLogin.removeStyleName("btn-primary");
			sendLogin.setEnabled(false);
		}
		else {
			sendLogin.removeStyleName("disabled");
			sendLogin.addStyleName("btn-primary");
			sendLogin.setEnabled(true);
		}
	}
	
	private void loginOrRegister(final String username) {
		if (!passwordOK || !usernameOK || (newaccount && !passwordRepeatOK)) return;
		
		final String pwd = passwordField.getText();
		
		if (newaccount) {
			if (password2Field.getText().equals(pwd)) {
				
				User u = new User();
        		u.setName(username);
        		u.setEncryptedPwd(pwd);
				Utils.query("POST", "user", u, new Utils.ReadyStateChangeReceiver() {
					@Override
					protected void error(String msg) {
						Window.alert("Something failed during registration. Please try again in a few minutes.");
					}

					@Override
					protected void succeeded(String msg) {
						JSONValue v = JSONParser.parseStrict(msg);
						
						if (v.isObject() != null) {
							Storage ss = Storage.getSessionStorageIfSupported();
							ss.setItem("username", username);
							ss.setItem("pwd", pwd);
							
							String encodedKey = v.isObject().get("encodedKey").toString();
							ss.setItem("encodedKey", encodedKey.substring(1, encodedKey.length() - 1));
							
							Window.Location.assign("/main.html");
						}
						else {
							Window.alert("Couldn't register...");
						}
					}
				});
				
            }
            else {
            	Window.alert("Passwords mismatch! Please, retype the password.");
            }
		}
		else {
			Utils.query("GET", "user", "name=" + username + "&encryptedPwd=" + pwd, new Utils.ReadyStateChangeReceiver() {
				@Override
				protected void error(String msg) {
					RootPanel.get("usernamewrapper").removeStyleName("error");
					RootPanel.get("usernamewrapper").removeStyleName("success");
					RootPanel.get("usernamewrapper").removeStyleName("info");
					
					RootPanel.get("usernamewrapper").addStyleName("error");
					
					preventLogin(true);
					usernameOK = false;
					sendLogin.setText("Fix username problem");
				}

				@Override
				protected void succeeded(String msg) {
					RootPanel.get("usernamewrapper").removeStyleName("error");
					RootPanel.get("usernamewrapper").removeStyleName("success");
					RootPanel.get("usernamewrapper").removeStyleName("info");
					
					JSONValue v = JSONParser.parseStrict(msg);
					if (v.isObject() != null) {
						Storage ss = Storage.getSessionStorageIfSupported();
						ss.setItem("username", username);
						ss.setItem("pwd", pwd);
						
						String encodedKey = v.isObject().get("encodedKey").toString();
						ss.setItem("encodedKey", encodedKey.substring(1, encodedKey.length() - 1));
						
						Window.Location.assign("/main.html");
					}
					else {
						Window.alert("Incorrect password");
					}
				}
			});
			
		}
	}

}
