package eltech.vkmessage.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;
import eltech.vkmessage.model.VkDialog;

public class NewMessagePanel extends JPanel implements ActionListener, KeyListener {
	
	private JTextField messageInputField;
	private JButton sendButton;
	
	private DialogPanel dialogPanel;
	
	public NewMessagePanel(DialogPanel dialogPanel) {
		//this.setPreferredSize(new Dimension(350, 40));
		messageInputField = new JTextField();
		messageInputField.setPreferredSize(new Dimension(300, 30));
		messageInputField.addKeyListener(this);
		sendButton = new JButton("Отправить");
		sendButton.setActionCommand("send");
		sendButton.addActionListener(this);
		sendButton.setMaximumSize(new Dimension(100, 30));
		this.dialogPanel = dialogPanel;
		
		this.setLayout(new FlowLayout());
		
		this.add(messageInputField);
		this.add(sendButton);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("send")) {
			onMessageSent(getMessage());
		}
	}

	private void onMessageSent(String message) {
		if (!isValidMessage(message)) {
			clearMessage();
			return;
		}
			
					
		String args = "";
		VkDialog dialog = dialogPanel.getModel().getDialog();
		if (dialog.isChat()) {
			args += "chat_id=" + dialog.getChatId();
		} else {
			args += "user_id=" + dialog.getUserId();
		}
		try {
			args += "&message=" + URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// NOP 
		}
		
		try {
			Application.INSTANCE.getConnectionManager().getConnection().executeApiMethod("messages.send", args);
			clearMessage();
			dialogPanel.getModel().updateMessages();
			dialogPanel.updateUI();
			dialogPanel.repaint();
		} catch (VkApiMethodException e) {
			e.printStackTrace();
		} catch (VkNoConnectionExcepion e) {
			e.printStackTrace();
		}
	}

	private boolean isValidMessage(String message) {
		if (getMessage().replaceAll(" ", "").equals("")) {
			return false;
		}
		return true;
	}

	private String getMessage() {
		return messageInputField.getText();
	}
	
	private void clearMessage() {
		messageInputField.setText("");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// sending message on ENTER
		if ( (e.getKeyCode() == KeyEvent.VK_ENTER) && (messageInputField.hasFocus()) )
			onMessageSent(getMessage());
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
