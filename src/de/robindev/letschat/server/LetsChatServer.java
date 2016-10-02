package de.robindev.letschat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.robindev.letschat.Constants;
import de.robindev.letschat.server.thread.ClientHandler;

/**
 * @author RobinDEV (09.09.2016, 22:37:52)
 */
public class LetsChatServer {

	private ServerSocket server;
	private List<PrintWriter> clients;

	public LetsChatServer() {
		if (start()) {
			listen();
		}
	}

	public void broadcast(String msg) {
		clients.stream().forEach(out -> {
			out.println(msg);
			out.flush();
		});
	}

	private void listen() {
		while (true) {
			try {
				Socket client = server.accept();
				PrintWriter out = new PrintWriter(
						new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
				clients.add(out);
				new ClientHandler(client, this).start();
				System.out.println("Client mit der IP-Adresse " + client.getInetAddress().getHostAddress()
						+ " hat die Verbindung aufgebaut!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean start() {
		try {
			server = new ServerSocket(Constants.PORT);
			clients = new ArrayList<>();
			System.out.println("Server erfolgreich gestartet. Warte auf Verbindungen...");
			return true;
		} catch (IOException ex) {
			System.err.println("Fehler aufgetreten (" + ex.getClass().getName() + ", " + ex.getMessage() + ")");
			System.exit(1);
			return false;
		}
	}

	public static void main(String[] args) {
		new LetsChatServer();
	}
}
