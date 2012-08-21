package edu.neumont.csc105.contactmanager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.Border;

public class ContactManager extends JFrame{
	private File contactFile;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JButton saveButton;
	private JButton loadButton;
	private JButton addUserButton;
	private JList users;
	private DefaultListModel model;
	public ContactManager(File f) {
		super("Contact Manager");
		if (f == null) {
			f = new File("contacts.csv");
		}
		initializeGUI(f);
		initButtonListeners();
	}

	private void doSave() {
		ArrayList<Person> peopleToSave=new ArrayList<Person>();
		for (int i = 0; i < model.getSize(); i++) {
			peopleToSave.add((Person) model.get(i));
		}
		
		contactFile.delete();
		File newContactList=new File("contacts.csv");
		FileWriter fWriter = null;
		BufferedWriter textWriter = null;
		try {
			fWriter=new FileWriter(newContactList);
			textWriter=new BufferedWriter(fWriter);
			for(Person p: peopleToSave){
				textWriter.write(p.toString()+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				textWriter.close();
				fWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e2){
				e2.printStackTrace();
			}
		}
		
	}
	
	private void doLoad(){
		JFileChooser chooser=new JFileChooser();
		int chosenAFile=chooser.showOpenDialog(null);
		
		if(chosenAFile==JFileChooser.APPROVE_OPTION){
			File selectedFile=chooser.getSelectedFile();
			if(Extension.getExtension(selectedFile).equals(Extension.csv)){
				contactFile=selectedFile;
				model.removeAllElements();
				parseUsers();
			}
		}
	}
	
	private void doAddUser(){
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String email = emailField.getText();
		if(!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()){
			Person p=new Person(firstName, lastName, email);
			model.addElement(p);
		}
	}
	
	private void initButtonListeners() {
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
		
	}

	private void initializeGUI(File f) {
		contactFile = f;
		this.setResizable(false);
		this.setBounds(50,50,800,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout gridLayout = new GridLayout(6,1,1,1);
		final JPanel userDataPanel=new JPanel(gridLayout);
		JPanel firstNamePanel=new JPanel();
		userDataPanel.add(firstNamePanel);
		JLabel firstNameLabel=new JLabel("FirstName");
		firstNameField=new JTextField(20);
		firstNamePanel.add(firstNameLabel,BorderLayout.WEST);
		firstNamePanel.add(firstNameField,BorderLayout.EAST);
		
		JPanel lastNamePanel=new JPanel();
		userDataPanel.add(lastNamePanel);
		JLabel lastNameLabel=new JLabel("LastName");
		lastNameField=new JTextField(20);
		lastNamePanel.add(lastNameLabel,BorderLayout.WEST);
		lastNamePanel.add(lastNameField,BorderLayout.EAST);
		
		JPanel emailPanel=new JPanel();
		userDataPanel.add(emailPanel);
		JLabel emailLabel=new JLabel("Email");
		emailField=new JTextField(20);
		emailPanel.add(emailLabel,BorderLayout.WEST);
		emailPanel.add(emailField,BorderLayout.EAST);
		
		addUserButton=new JButton("Add User");
		addUserButton.setBounds(0,0,100,50);
		userDataPanel.add(addUserButton);
		
		JPanel buttonPanel=new JPanel();
		saveButton=new JButton("Save");
		buttonPanel.add(saveButton);
		loadButton=new JButton("Load");
		buttonPanel.add(loadButton);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		this.add(userDataPanel,BorderLayout.WEST);
		model=new DefaultListModel();
		users=new JList(model);
		parseUsers();
		final JScrollPane userScrollPanel=new JScrollPane();
		userScrollPanel.getViewport().add(users);
		this.add(userScrollPanel,BorderLayout.EAST);
		this.setVisible(true);
	}
	
	
private void parseUsers(){
		ArrayList<Person> people=new ArrayList<Person>();
		FileReader fReader=null;
		BufferedReader textReader=null;
		try {
			fReader = new FileReader(contactFile);
			textReader=new BufferedReader(fReader);
			
			String readIn;
			while(textReader.ready() && (readIn=textReader.readLine())!=null && !readIn.isEmpty()){
				String[] userInfo=readIn.split(",");
				String email = userInfo[UserProperty.EMAIL.ordinal()];
				String lName = userInfo[UserProperty.LASTNAME.ordinal()];
				String fName = userInfo[UserProperty.FIRSTNAME.ordinal()];
				Person p=new Person(fName, lName, email);
				people.add(p);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(fReader!=null){
				try {
					fReader.close();
					textReader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		for(Person p: people){
			model.addElement(p);
		}
	}

	public static void main(String[] args) {
		ContactManager contactManager = new ContactManager(new File(
				"contacts.csv"));
	}
}
