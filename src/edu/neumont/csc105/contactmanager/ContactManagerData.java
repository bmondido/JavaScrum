package edu.neumont.csc105.contactmanager;

import java.io.File;

import javax.swing.JTextField;

public class ContactManagerData {
	private File contactFile;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;

	public ContactManagerData() {
	}

	public File getContactFile() {
		return contactFile;
	}

	public void setContactFile(File contactFile) {
		this.contactFile = contactFile;
	}

	public JTextField getFirstNameField() {
		return firstNameField;
	}

	public void setFirstNameField(JTextField firstNameField) {
		this.firstNameField = firstNameField;
	}

	public JTextField getLastNameField() {
		return lastNameField;
	}

	public void setLastNameField(JTextField lastNameField) {
		this.lastNameField = lastNameField;
	}

	public JTextField getEmailField() {
		return emailField;
	}

	public void setEmailField(JTextField emailField) {
		this.emailField = emailField;
	}
}