package edu.neumont.csc105.contactmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Box.Filler;
import javax.swing.border.Border;

public class ContactManager extends JFrame {
	private ContactManagerData data = new ContactManagerData();
	private UserList userList;

	public ContactManager(String fileName) {
		super("Contact Manager");
		File f;
		if (fileName == null || fileName.isEmpty()) {
			try {
				new File("contacts.csv").createNewFile();
				f = new File("contacts.csv");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				new File(fileName).createNewFile();
				f = new File(fileName);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		data.setContactFile(f);
		initializeGUI();
		initListeners();
	}

	private void doSave() {
		if (userList.getModel().getSize() > 0) {
			ArrayList<Person> peopleToSave = new ArrayList<Person>();
			for (int i = 0; i < userList.getModel().getSize(); i++) {
				peopleToSave.add((Person) userList.getModel().get(i));
			}
			String fileName = data.getContactFile().getName();
			data.getContactFile().delete();
			File newContactList = new File(fileName);
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
		if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !firstName.contains(",") && !lastName.contains(",") && !email.contains(",")) {
			Person p = new Person(firstName, lastName, email);
			if (!userList.getModel().contains(p)) {
				userList.getModel().addElement(p);
				doClearFields();
			} else {
				JOptionPane.showMessageDialog(null, "There's already a user with that information");
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "There's something wrong with the input");
		}
	}

	private void doEditUser(int index) {
		if (index > -1) {
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

	private void doDeleteUser(int index) {
		if (index > -1) {
			userList.getModel().remove(index);
			userList.getModel().trimToSize();
			doClearFields();
		}
	}

	private void doClearFields() {
		userList.setCurrentUserIndex(-1);
		JButtonFactory.getButton("delete").setVisible(false);
		JButtonFactory.getButton("add").setVisible(true);
		JButtonFactory.getButton("edit").setVisible(false);
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
				JButtonFactory.getButton("delete").setVisible(true);
				JButtonFactory.getButton("add").setVisible(false);
				JButtonFactory.getButton("edit").setVisible(true);
			}
		});
		JButtonFactory.getButton("save").addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						doSave();
					}

				});
		JButtonFactory.getButton("load").addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						doLoad();

					}
				});

		JButtonFactory.getButton("add").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doAddUser();
			}
		});

		JButtonFactory.getButton("edit").addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						doEditUser(userList.getCurrentUserIndex());

					}
				});

		JButtonFactory.getButton("clear").addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						doClearFields();

					}
				});

		JButtonFactory.getButton("delete").addActionListener(
				new ActionListener() {

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

		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		fileMenu.setShortcut(new MenuShortcut(KeyEvent.VK_F));
		MenuItem openItem = new MenuItem("Open");
		openItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doLoad();

			}
		});
		fileMenu.add(openItem);
		menuBar.add(fileMenu);
		this.setMenuBar(menuBar);

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

		JButtonFactory.putButton("add", new JButton("Add User"));
		JButtonFactory.getButton("add")
				.setPreferredSize(new Dimension(100, 50));
		JButtonFactory.putButton("edit", new JButton("Edit User"));
		JButtonFactory.getButton("edit").setPreferredSize(
				new Dimension(100, 50));
		JButtonFactory.getButton("edit").setVisible(false);
		JButtonFactory.putButton("clear", new JButton("Clear"));
		JButtonFactory.getButton("clear").setPreferredSize(
				new Dimension(100, 50));
		JButtonFactory.putButton("delete", new JButton("Delete User"));
		JButtonFactory.getButton("delete").setPreferredSize(
				new Dimension(100, 50));
		JButtonFactory.getButton("delete").setVisible(false);
		userDataPanel.add(JButtonFactory.getButton("clear"));
		userDataPanel.add(JButtonFactory.getButton("edit"));
		userDataPanel.add(JButtonFactory.getButton("add"));
		userDataPanel.add(JButtonFactory.getButton("delete"));

		JPanel buttonPanel = new JPanel();
		JButtonFactory.putButton("save", new JButton("Save"));
		JButtonFactory.getButton("save").setPreferredSize(
				new Dimension(100, 50));
		buttonPanel.add(JButtonFactory.getButton("save"));
		JButtonFactory.putButton("load", new JButton("Load"));
		JButtonFactory.getButton("load").setPreferredSize(
				new Dimension(100, 50));
		buttonPanel.add(JButtonFactory.getButton("load"));
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(userDataPanel, BorderLayout.WEST);

		userList = new UserList();
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
		ContactManager contactManager = new ContactManager("class.csv");
	}
}
