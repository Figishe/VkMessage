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
	private JPanel activeDialogPanelHolder = new JPanel();
	
	public MessengerFrame() {
		super();
		this.setSize(640, 480);
		this.setLocation(300, 200);
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
		this.activeDialogPanelHolder.removeAll();
		if (panel == null) {
			JPanel noActiveDialog = new JPanel();
			noActiveDialog.add(new JLabel("Ничего не открыто из диалогов..."));
			this.activeDialogPanelHolder.add(noActiveDialog);
		} else {
			this.activeDialogPanelHolder.add(new JScrollPane(panel));
			activeDialogPanelHolder.updateUI();
		}
	}
	
	public DialogPanel getActiveDialogPanel() {
		return (DialogPanel) this.activeDialogPanelHolder.getComponent(0);
	}
}
