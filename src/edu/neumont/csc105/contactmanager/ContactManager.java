package edu.neumont.csc105.contactmanager;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ContactManager extends JFrame{
	private File contactFile;
	
	public ContactManager(File f) {
		super("Contact Manager");
		if (f == null) {
			f = new File("contacts.csv");
		}
		initializeGUI(f);
	}

	private void initializeGUI(File f) {
		this.setResizable(false);
		this.setBounds(50,50,800,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel userDataPanel=new JPanel();
		this.add(userDataPanel,BorderLayout.WEST);
		final JPanel usersPanel=new JPanel();
		contactFile = f;
		JList<Person> users=new JList<Person>(parseUsers());
		this.add(usersPanel,BorderLayout.EAST);
		usersPanel.add(users);
		this.setVisible(true);
	}
	
	private Person[] parseUsers(){
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
		Person[] peopleToReturn = new Person[1];
		return people.toArray(peopleToReturn);
	}

	

	public static void main(String[] args) {
		ContactManager contactManager = new ContactManager(new File(
				"contacts.csv"));
	}
}
