package com.sdp.todolist.client;

import com.google.gwt.core.client.EntryPoint;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.sdp.todolist.shared.User;
import com.sdp.todolist.shared.Utils;
import com.sdp.todolist.shared.Task;

public class Main implements EntryPoint {
	
	private Label usernameLabel = new Label();
	
	private String usr;
	private String pwd;
	private String encodedKey;
	private VerticalPanel taskList;
	private HashMap<String,Task> map;
	
	private boolean hideCompletedStatus = false;
	private boolean hideUncompletedStatus = false;
	
	public void onModuleLoad() {
		usernameLabel.setText("Loading user data...");
		RootPanel.get("username").add(usernameLabel);
		
		Storage ss = Storage.getSessionStorageIfSupported();
		usr = ss.getItem("username");
		pwd = ss.getItem("pwd");
		
		if (usr == null) {
			Window.alert("There's no logged in user!");
			Window.Location.assign("/identification.html");
		}
		else {
			encodedKey = ss.getItem("encodedKey");
			loadPage();
		}
		
	}
	
	private void loadPage() {
		usernameLabel.setText("Logged in as: " + usr);
		
		Button logout = new Button("");
		RootPanel.get("logout").add(logout);
		logout.addStyleName("logout");
		
		Button editInfo = new Button("");
		RootPanel.get("editinfo").add(editInfo);
		editInfo.addStyleName("editinfo");
		
		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm("Are you sure you want to logout?")) {
					Storage ss = Storage.getSessionStorageIfSupported();
					ss.removeItem("username");
					ss.removeItem("password");
					ss.removeItem("encodedKey");
					Window.Location.assign("/identification.html");
				}
			}
		});
		
		editInfo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ChangeInfo rpass = new ChangeInfo(usr);
				rpass.center();
	            rpass.show();
			}
		});
		
		final Label hideCompletedLabel = new Label("Showing completed tasks");
		
		final Button hideCompleted = new Button();
		hideCompleted.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideCompletedStatus = !hideCompletedStatus;
				if (hideCompletedStatus == true) {
					hideCompleted.removeStyleName("showCom");
					hideCompleted.addStyleName("hideCom");
					hideCompletedLabel.setText("Hiding completed tasks");
				}
				else {
					hideCompleted.removeStyleName("hideCom");
					hideCompleted.addStyleName("showCom");
					hideCompletedLabel.setText("Showing completed tasks");
				}
				retrieveTasks();
			}
		});
		hideCompleted.addStyleName("showCom");
		
		
		RootPanel.get("hidecompleted").add(hideCompletedLabel);
		RootPanel.get("hidecompleted").add(hideCompleted);
		
		
		final Label hideUncompletedLabel = new Label("Showing uncompleted tasks");
		
		final Button hideUncompleted = new Button();
		hideUncompleted.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideUncompletedStatus = !hideUncompletedStatus;
				if (hideUncompletedStatus == true) {
					hideUncompleted.removeStyleName("showCom");
					hideUncompleted.addStyleName("hideCom");
					hideUncompletedLabel.setText("Hiding uncompleted tasks");
				}
				else {
					hideUncompleted.removeStyleName("hideCom");
					hideUncompleted.addStyleName("showCom");
					hideUncompletedLabel.setText("Showing uncompleted tasks");
				}
				retrieveTasks();
			}
		});
		hideUncompleted.addStyleName("showCom");
		
		
		RootPanel.get("hideuncompleted").add(hideUncompletedLabel);
		RootPanel.get("hideuncompleted").add(hideUncompleted);
		
		
		Button addTask = new Button("Add new task");
		addTask.addStyleName("btn btn-large btn-primary");
		RootPanel.get("newtask").add(addTask);
		
		addTask.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NewTaskDialog task = new NewTaskDialog(null);
				task.center();
				task.show();
			}
		});
		
		map = new HashMap<String, Task>();
		
		taskList = new VerticalPanel();
		RootPanel.get("tasklist").add(taskList);
		
		retrieveTasks();
	}
	
	public void retrieveTasks() {
		Utils.query("GET", "task", "userid=" + encodedKey, new Utils.ReadyStateChangeReceiver() {
			@Override
			protected void error(String msg) {
				Window.alert("There was an error. Please, try again in a few minutes.");
			}

			@Override
			protected void succeeded(String msg) {
				int count = taskList.getWidgetCount();
				for (int i = 0; i < count; ++i) {
					taskList.remove(0);
				}
				JSONValue v = JSONParser.parseStrict(msg);
				if (v.isArray() != null) {
					JSONArray array = v.isArray();
					for (int i = 0; i < array.size(); ++i) {
						JSONValue val = array.get(i);
						JSONObject obj = val.isObject();
						if (!Boolean.parseBoolean(obj.get("deleted").toString()) && ((Boolean.parseBoolean(obj.get("checked").toString()) && !hideCompletedStatus) || (!Boolean.parseBoolean(obj.get("checked").toString()) && !hideUncompletedStatus))) {
							String key = obj.get("encodedKey").toString();
							key = key.substring(1, key.length() - 1);
							String name = obj.get("name").toString();
							name = name.substring(1, name.length() - 1);
							String desc = obj.get("note").toString();
							desc = desc.substring(1, desc.length() - 1);
							Boolean completed = Boolean.parseBoolean(obj.get("checked").toString());
							Long dueTime = Long.parseLong(obj.get("dueTime").toString());
							Long priority = Long.parseLong(obj.get("priority").toString());
							
							createTask(key, name, desc, completed, dueTime, priority, null);
						}
					}
				}
				else {
					Window.alert("Couldn't get task list!");
				}
			}
		});
	}
	
	private class NewTaskDialog extends DialogBox {
		private TextBox taskname = new TextBox();
		private TextBox taskdesc = new TextBox();
		private Button okButton = new Button("Done");
		private Button cancelButton = new Button();
		private ListBox priority = new ListBox();
		private DatePicker datepicker = new DatePicker();
		
		private ListBox hour = new ListBox();
		private ListBox minute = new ListBox();
		private ListBox ampm = new ListBox();
		
		private Task task;
		private boolean hasBeenRestored;

		public NewTaskDialog(final Task task_) {
			super();
			
			setAnimationEnabled(true);
			
			for (int i = 0; i < 12; ++i) {
				hour.addItem(String.valueOf(i));
			}
			hour.addStyleName("compress");
			
			for (int i = 0; i < 60; ++i) {
				minute.addItem(String.valueOf(i));
			}
			minute.addStyleName("compress");
			
			ampm.addItem("AM");
			ampm.addItem("PM");
			ampm.addStyleName("compress");
			
			task = task_;
			
			if (task != null) {
				long time = task.getDueTime();
				Date d = new Date(time);
				int hours = d.getHours();
				int minutes = d.getMinutes();
				int ampm_ = (hours > 11) ? 1 : 0;
				if (hours > 11) hours -= 12;
				
				hour.setSelectedIndex(hours);
				minute.setSelectedIndex(minutes);
				ampm.setSelectedIndex(ampm_);
			}
			
			priority.addItem("low");
			priority.addItem("normal");
			priority.addItem("high");
			priority.setVisibleItemCount(1);
			
			if (task != null) {
				priority.setSelectedIndex(new Long(task.getPriority()).intValue());
			}
			else {
				priority.setSelectedIndex(1);
			}			

			setText("Task Details");

			taskname.getElement().setAttribute("placeholder", "Task Name");
			taskdesc.getElement().setAttribute("placeholder", "Task Description");		
			
			
			if (task != null) {
				taskname.setValue(task.getName());
				taskdesc.setValue(task.getNote());
				datepicker.setValue(new Date(task.getDueTime() + new Long(12*60*60*1000)));
				datepicker.setCurrentMonth(new Date(task.getDueTime() + new Long(12*60*60*1000)));
				
				priority.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						modifyTask();
					}
				});
				
				taskname.addBlurHandler(new BlurHandler() {
					@Override
					public void onBlur(BlurEvent event) {
						modifyTask();
					}
				});
				
				taskdesc.addBlurHandler(new BlurHandler() {
					@Override
					public void onBlur(BlurEvent event) {
						modifyTask();
					}
				});
				
				datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
					@Override
					public void onValueChange(ValueChangeEvent<Date> event) {
						modifyTask();
					}
				});
				
				hour.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						modifyTask();
					}
				});
				
				minute.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						modifyTask();
					}
				});
				
				ampm.addChangeHandler(new ChangeHandler(){
					@Override
					public void onChange(ChangeEvent event) {
						modifyTask();
					}
				});
				
				cancelButton.setText("Remove");				
			}
			else {
				Date curDate = new Date();
				@SuppressWarnings("deprecation")
				Date nextDay = new Date(curDate.getTime() + new Long(36*60*60*1000) - new Long(curDate.getHours()*60*60*1000) - new Long(curDate.getMinutes()*60*1000));
				datepicker.setValue(nextDay);
				datepicker.setCurrentMonth(nextDay);
				cancelButton.setText("Cancel");
			}
			
			RootPanel.get().add(datepicker);
			okButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					finish(false);
				}
			});

			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (task == null) {
						hide();
					}
					else {
						if (Window.confirm("Are you sure you want to remove the task?")) {
							
							Task t = generateTaskObject(task.getEncodedKey(), encodedKey, task.getName(), task.getNote(), task.isChecked(), task.getDueTime(), task.getPriority(), true);
							Utils.query("PUT", "task", t, new Utils.ReadyStateChangeReceiver() {
			    				@Override
			    				protected void error(String msg) {
			    					Window.alert("There was an error. Please, try again in a few minutes.");
			    				}

			    				@Override
			    				protected void succeeded(String msg) {
			    					JSONValue v = JSONParser.parseStrict(msg);

			    					if (v.isObject() == null) {
			    						Window.alert("Invalid task!");
			    					}
			    					else {
			    						taskList.remove(NewTaskDialog.this.task.getPanel());
			    						hide();
			    					}
			    				}
			    			});
						}
					}
				}
			});
			KeyUpHandler enterPressed = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						okButton.setFocus(true);
						finish(false);
					} else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						hide();
					}
				}
			};

			taskname.addKeyUpHandler(enterPressed);
			taskdesc.addKeyUpHandler(enterPressed);
			okButton.addStyleName("btn");
			okButton.addStyleName("btn-medium");
			okButton.addStyleName("btn-primary");

			cancelButton.addStyleName("btn");
			cancelButton.addStyleName("btn-medium");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.addStyleName("dialogVPanel");
			vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
			vPanel.add(taskname);
			vPanel.add(taskdesc);
			vPanel.add(priority);
			vPanel.add(datepicker);
			HorizontalPanel hDate = new HorizontalPanel();
			hDate.add(hour);
			hDate.add(minute);
			hDate.add(ampm);
			vPanel.add(hDate);
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(okButton);
			hPanel.add(cancelButton);
			vPanel.add(hPanel);
			setWidget(vPanel);
		}
		
		private void modifyTask() {
			Date d = new Date(datepicker.getValue().getTime());
			int hours = d.getHours();
			int minutes = d.getMinutes();
			
			final long calculatedTime = (datepicker.getValue().getTime() - new Long(hours*60*60*1000 + minutes*60*1000) + new Long(hour.getSelectedIndex()*60*60*1000) + new Long(minute.getSelectedIndex()*60*1000) + new Long(ampm.getSelectedIndex()*12));
			final Task t = generateTaskObject(task.getEncodedKey(), encodedKey, taskname.getValue(), taskdesc.getValue(), task.isChecked(), calculatedTime, priority.getSelectedIndex(), false);
			Utils.query("PUT", "task", t, new Utils.ReadyStateChangeReceiver() {
				@Override
				protected void error(String msg) {
					Window.alert("There was an error. Please, try again in a few minutes.");
				}

				@Override
				protected void succeeded(String msg) {
					JSONValue v = JSONParser.parseStrict(msg);

					if (v.isObject() == null) {
						if (v.isString() != null && v.isString().toString().equals("\"Provide a valid taskID to update a task\"")) {
							finish(true);
							hasBeenRestored = true;
						}
						else {
							Window.alert("Something went wrong and the task couldn't be updated. Try again in a few seconds.");
						}
					}
					else {
						createTask(t, task.getPanel());
						task = t;
					}
				}
			});
		}
		
		private void finish(final boolean restoring) {
			if (taskname.getValue().equals("")) {
				Window.alert("Taskname is empty, please type again");
			}
			else {
				Date d = new Date(datepicker.getValue().getTime());
				int hours = d.getHours();
				int minutes = d.getMinutes();
				final long time = (datepicker.getValue().getTime() - new Long(hours*60*60*1000 - minutes*60*1000) + hour.getSelectedIndex()*60*60*1000 + minute.getSelectedIndex()*60*1000 + ampm.getSelectedIndex()*12);
				
				if (task != null && !restoring) {
					if (hasBeenRestored) {
						taskList.remove(task.getPanel());
						
						createTask(task.getEncodedKey(), taskname.getValue(), taskdesc.getValue(), task.isChecked(), time, new Long(priority.getSelectedIndex()), null);
						RootPanel p = RootPanel.get("alerts");
						if (p.getWidgetCount() > 0) {
							p.remove(0);
						}
						p.add(new HTML("<div class=\"alert alert-success\"><b>Info</b>: The task you were editing was deleted. Calm down! We have restored it for your convenience.<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&#10008;</button></div>"));
					}
					hide();
					return;
				}
				
				final Task t = generateTaskObject(null, encodedKey, taskname.getValue(), taskdesc.getValue(), ((restoring) ? task.isChecked() : false), time, priority.getSelectedIndex(), false);
				Utils.query("POST", "task", t, new Utils.ReadyStateChangeReceiver() {

    				@Override
    				protected void error(String msg) {
    					Window.alert("There was an error. Please, try again in a few minutes.");
    				}

    				@Override
    				protected void succeeded(String msg) {
    					JSONValue v = JSONParser.parseStrict(msg);
    					if (v.isObject() != null) {
    						String key = v.isObject().get("encodedKey").toString();
    						key = key.substring(1, key.length() - 1);
    						if (!restoring) {
    							if (!hideUncompletedStatus) {
       								createTask(key, taskname.getValue(), taskdesc.getValue(), false, time , new Long(priority.getSelectedIndex()), null);
       								hide();
	       						}
	       						else {
	       							Window.alert("Task created. To see it, click the button to show uncompleted tasks");
	       						}
    						}
    						else {
    							task = createTask(key, taskname.getValue(), taskdesc.getValue(), task.isChecked(), time, new Long(priority.getSelectedIndex()), task.getPanel());
    						}
    					}
    					else {
    						Window.alert("Some error happened...");
    					}
    				}
    			});
			}
		}
		

		public void show() {
			super.show();
			taskname.setFocus(true);
			taskname.selectAll();
		}
	}
	
	private class ChangeInfo extends DialogBox {
		private TextBox username = new TextBox();
        private PasswordTextBox oldPass = new PasswordTextBox();
        private PasswordTextBox newPass = new PasswordTextBox();
        private PasswordTextBox confPass = new PasswordTextBox();
        private Button okButton = new Button("Done");
        private Button cancelButton = new Button("Cancel");

        public ChangeInfo(String usr) {
            super();
            setText("User information edit");
            
            username.setValue(usr);
            username.setEnabled(false);
            
            setAnimationEnabled(true);
            
            oldPass.getElement().setAttribute("placeHolder", "Current Password");
            newPass.getElement().setAttribute("placeHolder", "New Password");
            confPass.getElement().setAttribute("placeHolder", "New Password (confirmation)");

            okButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    finish();
                }
            });
            cancelButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    hide();
                }
            });
            
            KeyUpHandler enterPressed = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						finish();
					}
					else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						hide();
					}
				}
            };
            
            oldPass.addKeyUpHandler(enterPressed);
            newPass.addKeyUpHandler(enterPressed);
            confPass.addKeyUpHandler(enterPressed);
            
            okButton.addStyleName("btn");
            okButton.addStyleName("btn-medium");
            okButton.addStyleName("btn-primary");
            
            cancelButton.addStyleName("btn");
            cancelButton.addStyleName("btn-medium");
            
            Label cancel = new Label("I want to delete my account...");
            cancel.addStyleName("cancel");
            cancel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (Window.confirm("Are you sure you want to PERMANENTLY DELETE your account?")) {
						Window.alert("We are sorry that you want to leave us. Have fun in the scary, real world!");
						
						Utils.query("DELETE", "user", "encodedKey=" + encodedKey, new Utils.ReadyStateChangeReceiver() {
							@Override
							protected void error(String msg) {
								Window.alert("Something failed during deletion. Please try again in a few minutes.");
							}

							@Override
							protected void succeeded(String msg) {
								Storage ss = Storage.getSessionStorageIfSupported();
								ss.removeItem("username");
								ss.removeItem("password");
								ss.removeItem("encodedKey");
								
								Window.Location.assign("/identification.html");
							}
						});
					}					
				}
            });
            
            VerticalPanel vPanel = new VerticalPanel();
            vPanel.addStyleName("dialogVPanel");
            vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
            vPanel.add(username);
            vPanel.add(oldPass);
            vPanel.add(newPass);
            vPanel.add(confPass);
            HorizontalPanel hPanel = new HorizontalPanel();
            hPanel.add(okButton);
            hPanel.add(cancelButton);
            vPanel.add(hPanel);
            vPanel.add(cancel);
            setWidget(vPanel);
        }
        
        private void finish() {
        	if (!oldPass.getValue().equals(pwd)) {
        		Window.alert("Current password is not correct");
        		return;
        	}
        	
        	if (newPass.getValue().equals(confPass.getValue())) {
        		if (newPass.getValue().equals("")) {
        			hide();
        			return;
        		}
        		
        		User u = new User();
        		u.setEncodedKey(encodedKey);
        		u.setName(usr);
        		u.setEncryptedPwd(newPass.getValue());
        		Utils.query("PUT", "user", u, new Utils.ReadyStateChangeReceiver() {
    				@Override
    				protected void error(String msg) {
    					Window.alert("There was an error. Please, try again in a few minutes.");
    				}

    				@Override
    				protected void succeeded(String msg) {
    					JSONValue v = JSONParser.parseStrict(msg);
    					if (v.isObject() != null) {
    						hide();
    					}
    					else {
    						Window.alert("User doesn't exist!");
    					}
    				}
    			});
        		
        		return;
        	}
        	
        	Window.alert("Passwords do not match. Please, write them down again.");
        }
        
        public void show() {
        	super.show();
        	
        	oldPass.setFocus(true);
        	oldPass.selectAll();
        }
    }
	
	private Task generateTaskObject(final String key, final String userId, final String name, final String desc, final boolean completed, final long dueTime, final long priority, final boolean deleted) {
		final Task t = new Task();
		t.setEncodedKey(key);
		t.setName(name);
		t.setNote(desc);
		t.setDueTime(dueTime);
		t.setPriority(priority);
		t.setChecked(completed);
		t.setUserId(userId);
		t.setDeleted(deleted);
		
		return t;
	}
	
	private Task createTask(final String key, final String name, final String desc, final boolean completed, final long dueTime, final long priority, FocusPanel fp) {
		final Task t = generateTaskObject(key, encodedKey, name, desc, completed, dueTime, priority, false);
		
		createTask(t, fp);
		
		return t;
	}
	
	private void createTask(final Task t, FocusPanel fp) {
		map.put(t.getEncodedKey(), t);
		
		String prio = null;
		if (t.getPriority() == 2) {
			prio = "<span class=\"badge badge-important\">High</span>";
		}
		else if (t.getPriority() == 0) {
			prio = "<span class=\"badge badge-warning\">Low</span>";
		}
		else {
			prio = "";
		}
		
		FocusPanel taskpanel = new FocusPanel();
		taskpanel.addStyleName("clickable");
		HTMLPanel task = null;
		
		Date currentDate = new Date();
		String dateStr = DateTimeFormat.getFormat("M/d/y h:ma").format(new Date(t.getDueTime()));
		if (currentDate.getTime() > t.getDueTime()) {
			task = new HTMLPanel("<span class=\"datetime\"><span class=\"label label-warning\">Warning!</span>&nbsp;" + dateStr + "</span>");
		}
		else {
			task = new HTMLPanel("<span class=\"datetime\">" + dateStr + "</span>");
		}
		taskpanel.add(task);
		taskpanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				NewTaskDialog taskDialog = new NewTaskDialog(t);
				taskDialog.center();
				taskDialog.show();
			}
		});
		task.addStyleName("well");
		task.addStyleName("well-small");
		HTMLPanel tasknameandpriority = new HTMLPanel("");
		tasknameandpriority.addStyleName("tasknameandpriority");
		HTML html = new HTML("<span class=\"taskname\">" + t.getName() + "</span>" + prio);
		task.add(tasknameandpriority);
		
		final CheckBox cb = new CheckBox();
		cb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Task t2 = generateTaskObject(t.getEncodedKey(), encodedKey, t.getName(), t.getNote(), cb.getValue(), t.getDueTime(), t.getPriority(), false);
				Utils.query("PUT", "task", t2, new Utils.ReadyStateChangeReceiver() {
    				@Override
    				protected void error(String msg) {
    					Window.alert("There was an error. Please, try again in a few minutes.");
    				}

    				@Override
    				protected void succeeded(String msg) {
    					JSONValue v = JSONParser.parseStrict(msg);
    					if (v.isObject() == null) {
    						Window.alert("Some error happened...");
    					}
    					else {
    						if (hideCompletedStatus && cb.getValue()) {
    							taskList.remove(t.getPanel());
    						}
    						else if (hideUncompletedStatus && !cb.getValue()) {
    							taskList.remove(t.getPanel());
    						}
    					}
    				}
    			});
				event.stopPropagation();
			}
		});
		cb.setValue(t.isChecked());

		tasknameandpriority.add(cb);
		tasknameandpriority.add(html);
		
		if (fp != null) {
			taskList.insert(taskpanel, taskList.getWidgetIndex(fp));
			taskList.remove(taskList.getWidgetIndex(fp));
		}
		else {
			taskList.add(taskpanel);
		}
		
		t.setPanel(taskpanel);
	}
	
}
