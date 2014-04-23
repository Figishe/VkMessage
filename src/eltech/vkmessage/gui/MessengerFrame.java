package eltech.vkmessage.gui;

import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;

public class MessengerFrame extends JFrame {

	private DialogListPanel dialogListPanel;
	private DialogPanelHolder activeDialogPanelHolder = new DialogPanelHolder();
	
	public MessengerFrame() {
		super();
		this.setSize(1000, 500);
		this.setTitle("VkMessage");
		this.setLocation(200, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(0, 2));
		initializeComponents();
	}
	
	private void initializeComponents() {
		dialogListPanel = new DialogListPanel();
		dialogListPanel.update();
		this.add(new JScrollPane(dialogListPanel));
		this.setActiveDialogPanel(null);
		this.add(activeDialogPanelHolder);
	}
	
	public void setActiveDialogPanel(DialogPanel panel) {
		DialogPanel activeDialogPanel = getActiveDialogPanel();
		if (activeDialogPanel != null) {
			activeDialogPanel.close();
		}
		if (panel == null) {
			JPanel noActiveDialog = new JPanel();
			noActiveDialog.add(new JLabel("Выберите диалог из списка слева, чтобы начать общение"));
			this.activeDialogPanelHolder.setDefaultPanel(noActiveDialog);
		} else {
			this.activeDialogPanelHolder.setPanel(panel);
			activeDialogPanelHolder.updateUI();
		}
	}
	
	public DialogPanel getActiveDialogPanel() {
		try {
			return activeDialogPanelHolder.getPanel();
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		
	}
}

class DialogPanelHolder extends JPanel  {
	private DialogPanel panel = null;
	private JPanel defaultPanel = null;
	
	public void setDefaultPanel(JPanel panel) {
		this.defaultPanel = panel;
		this.removeAll();
		this.add(panel);
	}
	
	public void setPanel(DialogPanel panel) {
		this.panel = panel;
		this.removeAll();
		this.add(panel);
	}
	
	public DialogPanel getPanel() {
		return panel;
	}
}
