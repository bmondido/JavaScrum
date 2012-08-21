package edu.neumont.csc105.contactmanager;

import java.io.File;

public class Extension {
		static final String jpeg = "jpeg";
		static final String jpg = "jpg";
		static final String gif = "gif";
		static final String png = "png";
		static final String csv = "csv";

		public static String getExtension(File f) {
			String extend = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');
			if (i > 0 && i < s.length() - 1) {
				extend = s.substring(i + 1).toLowerCase();
			}
			return extend;
		}

	}