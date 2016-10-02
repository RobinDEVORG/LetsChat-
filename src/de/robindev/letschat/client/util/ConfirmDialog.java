package de.robindev.letschat.client.util;

import javax.swing.JOptionPane;

/**
 * @author RobinDEV (10.09.2016, 12:26:16)
 */
public abstract class ConfirmDialog {

	public static boolean show(String msg) {
		return JOptionPane.showConfirmDialog(null, msg) == JOptionPane.OK_OPTION;
	}
}
