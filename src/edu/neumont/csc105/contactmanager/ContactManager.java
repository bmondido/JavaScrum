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
	private File contactFile;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JButton saveButton;
	private JButton loadButton;
	private JButton addUserButton;
	private JButton editButton;
	private JButton clearButton;
	private JList users;
	private DefaultListModel model;
	private int currentUserIndex = -1;
	private JButton deleteButton;

	public ContactManager(File f) {
		super("Contact Manager");
		if (f == null) {
			f = new File("contacts.csv");
		}
		contactFile = f;
		initializeGUI();
		initListeners();
	}

	private void doSave() {
		ArrayList<Person> peopleToSave = new ArrayList<Person>();
		for (int i = 0; i < model.getSize(); i++) {
			peopleToSave.add((Person) model.get(i));
		}

		contactFile.delete();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				textWriter.close();
				fWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
		}

	}

	private void doLoad() {
		JFileChooser chooser = new JFileChooser();
		int chosenAFile = chooser.showOpenDialog(null);

		if (chosenAFile == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (Extension.getExtension(selectedFile).equals(Extension.csv)) {
				contactFile = selectedFile;
				model.removeAllElements();
				parseUsers();
			}
		}
	}

	private void doAddUser() {
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String email = emailField.getText();
		if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
			Person p = new Person(firstName, lastName, email);
			if (currentUserIndex < 0) {
				model.addElement(p);
			}
		}
	}

	private void doEditUser(int index) {
		if(index>-1){
			Person p = (Person) model.get(index);
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String email = emailField.getText();
			if (!firstName.isEmpty() && !firstName.equals(p.getFirstName())) {
				p.setFirstName(firstName);
			}
			if (!lastName.isEmpty() && !lastName.equals(p.getLastName())) {
				p.setLastName(lastName);
			}
			if (!email.isEmpty() && !email.equals(p.getEmail())) {
				p.setEmail(email);
			}
	
			model.set(index, p);
		}
	}
	
	private void doDeleteUser(int index){
		if(index>-1){
			model.remove(index);
			model.trimToSize();
			currentUserIndex=-1;
			deleteButton.setVisible(false);
			addUserButton.setVisible(true);
			editButton.setVisible(false);
		}
	}

	private void doClearFields() {
		currentUserIndex = -1;
		deleteButton.setVisible(false);
		addUserButton.setVisible(true);
		editButton.setVisible(false);
		firstNameField.setText("");
		lastNameField.setText("");
		emailField.setText("");
	}

	private void initListeners() {

		users.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = users.locationToIndex(e.getPoint());
				Person p = (Person) model.get(index);
				firstNameField.setText(p.getFirstName());
				lastNameField.setText(p.getLastName());
				emailField.setText(p.getEmail());
				currentUserIndex = index;
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
				doEditUser(currentUserIndex);

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
				doDeleteUser(currentUserIndex);
				
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
		firstNameField = new JTextField(20);
		firstNamePanel.add(firstNameLabel, BorderLayout.WEST);
		firstNamePanel.add(firstNameField, BorderLayout.EAST);

		JPanel lastNamePanel = new JPanel();
		userDataPanel.add(lastNamePanel);
		JLabel lastNameLabel = new JLabel("LastName");
		lastNameField = new JTextField(20);
		lastNamePanel.add(lastNameLabel, BorderLayout.WEST);
		lastNamePanel.add(lastNameField, BorderLayout.EAST);

		JPanel emailPanel = new JPanel();
		userDataPanel.add(emailPanel);
		JLabel emailLabel = new JLabel("Email");
		emailField = new JTextField(20);
		emailPanel.add(emailLabel, BorderLayout.WEST);
		emailPanel.add(emailField, BorderLayout.EAST);

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
		model = new DefaultListModel();
		users = new JList(model);
		parseUsers();
		final JScrollPane userScrollPanel = new JScrollPane();
		userScrollPanel.getViewport().add(users);
		this.add(userScrollPanel, BorderLayout.EAST);
		this.setVisible(true);
	}

	private void parseUsers() {
		ArrayList<Person> people = new ArrayList<Person>();
		FileReader fReader = null;
		BufferedReader textReader = null;
		try {
			fReader = new FileReader(contactFile);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			model.addElement(p);
		}
	}

	public static void main(String[] args) {
		ContactManager contactManager = new ContactManager(new File(
				"contacts.csv"));
	}
}
