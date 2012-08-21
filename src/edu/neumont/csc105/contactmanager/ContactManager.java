package edu.neumont.csc105.contactmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.Border;

public class ContactManager extends JFrame {
	private ContactManagerData data = new ContactManagerData();
	private JButton saveButton;
	private JButton loadButton;
	private JButton addUserButton;
	private JButton editButton;
	private JButton clearButton;
	private JButton deleteButton;
	private UserList userList;

	public ContactManager(File f) {
		super("Contact Manager");
		if (f == null) {
			f = new File("contacts.csv");
		}
		data.setContactFile(f);
		initializeGUI();
		initListeners();
	}

	private void doSave() {
		if(userList.getModel().getSize()>0){
			ArrayList<Person> peopleToSave = new ArrayList<Person>();
			for (int i = 0; i < userList.getModel().getSize(); i++) {
				peopleToSave.add((Person) userList.getModel().get(i));
			}
	
			data.getContactFile().delete();
			File newContactList = new File("contacts.csv");
			FileWriter fWriter = null;
			BufferedWriter textWriter = null;
			try {
				fWriter = new FileWriter(newContactList);
				textWriter = new BufferedWriter(fWriter);
				for (Person p : peopleToSave) {
					textWriter.write(p.toString() + "\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					textWriter.close();
					fWriter.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (NullPointerException e2) {
					throw new RuntimeException(e2);
				}
			}
		}
	}

	private void doLoad() {
		JFileChooser chooser = new JFileChooser();
		int chosenAFile = chooser.showOpenDialog(null);

		if (chosenAFile == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (Extension.getExtension(selectedFile).equals(Extension.csv)) {
				data.setContactFile(selectedFile);
				userList.getModel().removeAllElements();
				parseUsers();
			}
		}
	}

	private void doAddUser() {
		String firstName = data.getFirstNameField().getText();
		String lastName = data.getLastNameField().getText();
		String email = data.getEmailField().getText();
		if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
			Person p = new Person(firstName, lastName, email);
			if (userList.getCurrentUserIndex() < 0) {
				userList.getModel().addElement(p);
				doClearFields();
			}
		}
	}

	private void doEditUser(int index) {
		if(index>-1){
			Person p = (Person) userList.getModel().get(index);
			String firstName = data.getFirstNameField().getText();
			String lastName = data.getLastNameField().getText();
			String email = data.getEmailField().getText();
			if (!firstName.isEmpty() && !firstName.equals(p.getFirstName())) {
				p.setFirstName(firstName);
			}
			if (!lastName.isEmpty() && !lastName.equals(p.getLastName())) {
				p.setLastName(lastName);
			}
			if (!email.isEmpty() && !email.equals(p.getEmail())) {
				p.setEmail(email);
			}
	
			userList.getModel().set(index, p);
		}
	}
	
	private void doDeleteUser(int index){
		if(index>-1){
			userList.getModel().remove(index);
			userList.getModel().trimToSize();
			doClearFields();
		}
	}

	private void doClearFields() {
		userList.setCurrentUserIndex(-1);
		deleteButton.setVisible(false);
		addUserButton.setVisible(true);
		editButton.setVisible(false);
		clearTextFields();
	}

	private void clearTextFields() {
		data.getFirstNameField().setText("");
		data.getLastNameField().setText("");
		data.getEmailField().setText("");
	}

	private void initListeners() {

		userList.getUsers().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = userList.getUsers().locationToIndex(e.getPoint());
				Person p = (Person) userList.getModel().get(index);
				data.getFirstNameField().setText(p.getFirstName());
				data.getLastNameField().setText(p.getLastName());
				data.getEmailField().setText(p.getEmail());
				userList.setCurrentUserIndex(index);
				deleteButton.setVisible(true);
				addUserButton.setVisible(false);
				editButton.setVisible(true);
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doSave();
			}

		});
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doLoad();

			}
		});

		addUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doAddUser();
			}
		});

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doEditUser(userList.getCurrentUserIndex());

			}
		});

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doClearFields();

			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doDeleteUser(userList.getCurrentUserIndex());
				
			}
		});
	}

	private void initializeGUI() {
		this.setResizable(false);
		this.setBounds(50, 50, 800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel userDataPanel = new JPanel(new GridLayout(15, 1, 1, 1));
		JPanel firstNamePanel = new JPanel();
		userDataPanel.add(firstNamePanel);
		JLabel firstNameLabel = new JLabel("FirstName");
		data.setFirstNameField(new JTextField(20));
		firstNamePanel.add(firstNameLabel, BorderLayout.WEST);
		firstNamePanel.add(data.getFirstNameField(), BorderLayout.EAST);

		JPanel lastNamePanel = new JPanel();
		userDataPanel.add(lastNamePanel);
		JLabel lastNameLabel = new JLabel("LastName");
		data.setLastNameField(new JTextField(20));
		lastNamePanel.add(lastNameLabel, BorderLayout.WEST);
		lastNamePanel.add(data.getLastNameField(), BorderLayout.EAST);

		JPanel emailPanel = new JPanel();
		userDataPanel.add(emailPanel);
		JLabel emailLabel = new JLabel("Email");
		data.setEmailField(new JTextField(20));
		emailPanel.add(emailLabel, BorderLayout.WEST);
		emailPanel.add(data.getEmailField(), BorderLayout.EAST);

		addUserButton = new JButton("Add User");
		addUserButton.setPreferredSize(new Dimension(100, 50));
		editButton = new JButton("Edit User");
		editButton.setPreferredSize(new Dimension(100, 50));
		editButton.setVisible(false);
		clearButton = new JButton("Clear Fields");
		clearButton.setPreferredSize(new Dimension(100, 50));
		deleteButton = new JButton("DeleteUser");
		deleteButton.setPreferredSize(new Dimension(100, 50));
		deleteButton.setVisible(false);
		userDataPanel.add(clearButton);
		userDataPanel.add(editButton);
		userDataPanel.add(addUserButton);
		userDataPanel.add(deleteButton);

		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save");
		saveButton.setPreferredSize(new Dimension(100, 50));
		buttonPanel.add(saveButton);
		loadButton = new JButton("Load");
		loadButton.setPreferredSize(new Dimension(100, 50));
		buttonPanel.add(loadButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(userDataPanel, BorderLayout.WEST);
		
		userList=new UserList();
		parseUsers();
		final JScrollPane userScrollPanel = new JScrollPane();
		userScrollPanel.getViewport().add(userList.getUsers());
		this.add(userScrollPanel, BorderLayout.EAST);
		this.setVisible(true);
	}

	private void parseUsers() {
		ArrayList<Person> people = new ArrayList<Person>();
		FileReader fReader = null;
		BufferedReader textReader = null;
		try {
			fReader = new FileReader(data.getContactFile());
			textReader = new BufferedReader(fReader);

			String readIn;
			while (textReader.ready()
					&& (readIn = textReader.readLine()) != null
					&& !readIn.isEmpty()) {
				String[] userInfo = readIn.split(",");
				String email = userInfo[UserProperty.EMAIL.ordinal()];
				String lName = userInfo[UserProperty.LASTNAME.ordinal()];
				String fName = userInfo[UserProperty.FIRSTNAME.ordinal()];
				Person p = new Person(fName, lName, email);
				people.add(p);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fReader != null) {
				try {
					fReader.close();
					textReader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		for (Person p : people) {
			userList.getModel().addElement(p);
		}
	}

	public static void main(String[] args) {
		ContactManager contactManager = new ContactManager(new File(
				"contacts.csv"));
	}
}
