package de.robindev.letschat.server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import de.robindev.letschat.server.LetsChatServer;

/**
 * @author RobinDEV (09.09.2016, 22:40:19)
 */
public class ClientHandler extends Thread {

	private BufferedReader in;
	private LetsChatServer server;
	private Socket socket;

	public ClientHandler(Socket socket, LetsChatServer server) {
		this.socket = socket;
		this.server = server;
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
				server.broadcast(msg);
			}
		} catch (IOException ex) {
			if (ex.getMessage().equals("Connection reset")) {
				System.out.println("Client mit der IP-Adresse " + socket.getInetAddress().getHostAddress()
						+ " hat die Verbindung getrennt.");
			} else {
				System.err.println("Fehler aufgetreten (" + ex.getClass().getName() + ", " + ex.getMessage() + ")");
			}
		}
	}
}
