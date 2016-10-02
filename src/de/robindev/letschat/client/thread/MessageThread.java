package de.robindev.letschat.client.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.swing.JTextArea;

/**
 * @author RobinDEV (09.09.2016, 23:40:54)
 */
public class MessageThread extends Thread {

	private BufferedReader in;
	private JTextArea area;

	public MessageThread(Socket socket, JTextArea area) {
		this.area = area;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String msg;

		try {
			while ((msg = in.readLine()) != null) {
				area.append(msg + "\n");
				area.setCaretPosition(area.getText().length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
