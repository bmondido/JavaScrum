package edu.neumont.csc105.contactmanager;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class UserList {
	private JList users;
	private DefaultListModel model;
	private int currentUserIndex = -1;
	
	public UserList() {
		model=new DefaultListModel();
		users=new JList(model);
	}
	public JList getUsers() {
		return users;
	}
	public DefaultListModel getModel() {
		return model;
	}
	public int getCurrentUserIndex() {
		return currentUserIndex;
	}
	public void setCurrentUserIndex(int index) {
		currentUserIndex=index;
		
	}
	
	
}
