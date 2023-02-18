package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Check {

	public static boolean checkNumber(JTextField tf, String input) {
		if (!checkEmpty(tf, input))
			return false;
		String text = tf.getText().trim();
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(null, input + " needs to be integer");
			tf.grabFocus();
			return false;
		}
	}

	public static boolean checkPositive(JTextField tf, String input) {
		if (!checkNumber(tf, input))
			return false;
		String text = tf.getText().trim();

		if (0 >= Integer.parseInt(text)) {
			JOptionPane.showMessageDialog(null, input + " must be positive");
			tf.grabFocus();
			return false;
		}
		return true;
	}

	public static boolean checkMonth(JTextField tf, String input) {
		if (!checkNumber(tf, input))
			return false;
		String text = tf.getText().trim();

		if (Integer.parseInt(text) > 12 || Integer.parseInt(text) < 1) {
			JOptionPane.showMessageDialog(null, input + " must be between 1 and 12");
			tf.grabFocus();
			return false;
		}
		return true;
	}

	public static boolean checkEmpty(JTextField tf, String input) {
		String text = tf.getText().trim();
		if (0 == text.length()) {
			JOptionPane.showMessageDialog(null, input + " can't be empty");
			tf.grabFocus();
			return false;
		}
		return true;

	}
	
	public static boolean isValid(String text) {
		if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
			return false;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		try {
			df.parse(text);
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	public static int getInt(JTextField tf) {
		return Integer.parseInt(tf.getText());
	}

}