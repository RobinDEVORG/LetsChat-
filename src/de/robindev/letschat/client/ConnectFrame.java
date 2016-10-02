package de.robindev.letschat.client;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.robindev.letschat.Constants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/**
 * @author RobinDEV (09.09.2016, 23:00:14)
 */
@SuppressWarnings("serial")
public class ConnectFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectFrame frame = new ConnectFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConnectFrame() {

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ConnectFrame.class.getResource("/de/robindev/letschat/res/icon.png")));
		setResizable(false);
		setTitle("LetsChat! | Verbindung aufbauen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 66);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblIp = new JLabel("IP:");

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connect();
			}
		});
		textField.setColumns(10);

		JButton btnVerbinden = new JButton("Verbinden");
		btnVerbinden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connect();
			}
		});

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(5)
				.addComponent(lblIp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(21)
				.addComponent(textField, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE).addGap(10)
				.addComponent(btnVerbinden, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE).addGap(10)
				.addComponent(btnAbbrechen, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE).addGap(16)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(4)
								.addComponent(lblIp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(10))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(textField,
								GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(btnVerbinden))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(btnAbbrechen)));
		contentPane.setLayout(gl_contentPane);
	}

	private void connect() {
		try {
			Socket socket = new Socket(textField.getText(), Constants.PORT);
			dispose();
			new LetsChatClient(socket).setVisible(true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Verbindung konnte nicht hergestellt werden!");
		}
	}

}
