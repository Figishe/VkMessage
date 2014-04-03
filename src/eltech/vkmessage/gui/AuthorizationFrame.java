package eltech.vkmessage.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import eltech.vkmessage.connection.VkConnectionManager;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;


public class AuthorizationFrame extends JFrame {
	
	public AuthorizationFrame(final VkConnectionManager connectionManager, String defaultLogin) {
		this.setLayout(new FlowLayout());
		this.setSize(400, 115);
		this.setLocation(400, 350);
		this.setTitle("Авторизация");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		final JTextField loginField = new JTextField(defaultLogin);
		final JPasswordField passwordField = new JPasswordField();
		loginField.setPreferredSize(new Dimension(180, 24));
		passwordField.setPreferredSize(new Dimension(180, 24));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		JPanel loginPanel = new JPanel();
		loginPanel.setAlignmentX(RIGHT_ALIGNMENT);
		loginPanel.add(new JLabel("Логин: "));
		loginPanel.add(loginField);
		panel.add(loginPanel);
		JPanel passwordPanel = new JPanel();
		passwordPanel.setAlignmentX(RIGHT_ALIGNMENT);
		passwordPanel.add(new JLabel("Пароль: "));
		passwordPanel.add(passwordField);
		panel.add(passwordPanel);
		
		this.add(panel);
		
		JButton loginButton = new JButton("Войти");
		final JFrame thisFrame = this;
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					connectionManager.connect(4081051, loginField.getText(), new String(passwordField.getPassword()));
					Application.INSTANCE.onLoginSucceed();
					thisFrame.dispose();
				} catch (VkNoConnectionExcepion e1) {
					//TODO: сообщение пользователю
					JOptionPane.showMessageDialog(thisFrame,
						    e1.getMessage(),
						    "Ошибка при подключении",
						    JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		this.add(loginButton);
		this.setVisible(true);
	}
	
	public AuthorizationFrame(VkConnectionManager connectionManager) {
		this(connectionManager, "");
	}
}
