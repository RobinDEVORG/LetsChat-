package de.robindev.letschat.client;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.robindev.letschat.client.util.ConfirmDialog;
import de.robindev.letschat.client.util.XMLConfig;

/**
 * @author RobinDEV (10.09.2016, 00:38:14)
 */
@SuppressWarnings("serial")
public class OptionsFrame extends JFrame {

	private JPanel contentPane;
	private JComboBox<String> comboBox;

	private XMLConfig data;
	private Attr font;

	public OptionsFrame() {

		data = new XMLConfig(new File("config.xml"));

		NodeList designNodes = data.getNodeList("design");

		if (!data.elementExists("config")) {
			Element root = data.addElement("config");
			Element design = data.addElement(root, "design");
			font = data.setAttribute(design, "font", "Segoe ui");
			data.save();
		} else {
			Element design = (Element) designNodes.item(0);
			font = design.getAttributeNode("font");
		}

		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(OptionsFrame.class.getResource("/de/robindev/letschat/res/icon.png")));
		setTitle("LetsChat! | Optionen");
		setBounds(100, 100, 367, 103);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Design", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(26, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE).addContainerGap()));
		panel.setLayout(null);

		comboBox = new JComboBox<>();

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = env.getAllFonts();
		for (Font font : fonts) {
			comboBox.addItem(font.getName());
		}

		comboBox.setSelectedItem(font.getValue());
		comboBox.setFont(getFont());

		comboBox.setBounds(66, 22, 142, 20);
		panel.add(comboBox);

		JLabel lblSchrift = new JLabel("Schrift:");
		lblSchrift.setBounds(10, 25, 46, 14);
		panel.add(lblSchrift);

		JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!font.getValue().equals(comboBox.getSelectedItem())) {
					font.setValue((String) comboBox.getSelectedItem());
					data.save();

					if (ConfirmDialog.show(
							"Du musst LetsChat jetzt neustarten, damit die Änderungen übernommen werden.\nMöchtest du das jetzt tun?")) {
						restart();
					}
				}
				setVisible(false);
			}
		});
		btnSpeichern.setBounds(216, 21, 89, 23);
		panel.add(btnSpeichern);
		contentPane.setLayout(gl_contentPane);
	}

	private void restart() {
		try {
			String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(ConnectFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			if (!currentJar.getName().endsWith(".jar"))
				return;

			List<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			System.exit(0);
		} catch (Exception ex) {

		}
	}

	public Font getFont() {
		return new Font(font.getValue(), Font.PLAIN, 13);
	}
}
