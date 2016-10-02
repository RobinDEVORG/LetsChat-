package de.robindev.letschat.client;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import de.robindev.letschat.client.thread.MessageThread;

/**
 * @author RobinDEV (09.09.2016, 22:59:59)
 */
@SuppressWarnings("serial")
public class LetsChatClient extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextArea taIncMessage;
	private PrintWriter out;
	private OptionsFrame options;
	public static Font FONT;
	private JTextField txtMessage;

	public LetsChatClient(Socket socket) {
		options = new OptionsFrame();
		FONT = options.getFont();

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(LetsChatClient.class.getResource("/de/robindev/letschat/res/icon.png")));
		setTitle("LetsChat!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 789, 580);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("Datei");
		menuBar.add(mnFile);

		JMenuItem miSettings = new JMenuItem("Optionen");
		miSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (options.isVisible()) {
					options.requestFocus();
				} else {
					options.setVisible(true);
				}
			}
		});
		mnFile.add(miSettings);

		JMenuItem miExit = new JMenuItem("Beenden");
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		mnFile.add(miExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(FONT);

		JScrollPane scrollPane = new JScrollPane();

		txtName = new JTextField();
		txtName.setFont(FONT);
		txtName.setColumns(10);

		JLabel lblMessage = new JLabel("Nachricht:");
		lblMessage.setFont(FONT);

		JButton btnSend = new JButton("Abschicken");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendMessage();
			}
		});
		btnSend.setFont(LetsChatClient.FONT);

		txtMessage = new JTextField();
		txtMessage.setFont(LetsChatClient.FONT);
		txtMessage.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(10)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblName)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(lblMessage).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSend)))
				.addGap(10)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblName)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSend).addComponent(lblMessage).addComponent(txtMessage,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(11)));

		taIncMessage = new JTextArea();
		taIncMessage.setEditable(false);
		taIncMessage.setFont(LetsChatClient.FONT);
		scrollPane.setViewportView(taIncMessage);
		contentPane.setLayout(gl_contentPane);

		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

		new MessageThread(socket, taIncMessage).start();
	}

	private void sendMessage() {
		if (txtName.getText().trim().length() > 0 && txtMessage.getText().trim().length() > 0) {
			if (txtName.isEnabled()) {
				txtName.setEnabled(false);
			}

			setTitle("LetsChat! (Dein Name: " + txtName.getText() + ")");

			out.println(txtName.getText() + ": " + txtMessage.getText());
			out.flush();
		}
	}
}
