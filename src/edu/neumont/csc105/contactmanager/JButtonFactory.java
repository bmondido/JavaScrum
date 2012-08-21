package edu.neumont.csc105.contactmanager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

public class JButtonFactory {

	private static Map<String,JButton> buttons=new HashMap<String, JButton>();
	
	public static JButton getButton(String key){
		JButton buttontoReturn=null;
		if(buttons.containsKey(key)){
			buttontoReturn=buttons.get(key);
		}
		return buttontoReturn;
	}
	
	public static void putButton(String key,JButton button){
		buttons.put(key, button);
	}
	
}
