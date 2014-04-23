package eltech.vkmessage.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import eltech.vkmessage.model.DialogPanelModel;
import eltech.vkmessage.model.VkMessage;

/**
 * This class represents GUI view of messages from dialog,
 * it controls their position and order in the component
 *
 */

public class DialogMessagesPanel extends JPanel {
	private DialogPanelModel model;
	private List<MessagePanel> messagePanels;
	
	public DialogMessagesPanel(DialogPanelModel model) {
		super();
		this.model = model;
		this.setLayout(new GridLayout(0, 1));
		this.setPreferredSize(new Dimension(500, 350));
		this.messagePanels = new LinkedList<MessagePanel>();
	}
	
	private void addMessageHeader(MessagePanel messagePanel) {
		messagePanels.add(messagePanel);
		this.add(messagePanel);
	}
	
	public void update() {
		model.updateMessages();
		
		
		List<VkMessage> messages = model.getMessages();

		for (int i = messages.size() - 1; i >= 0; i--) {
			VkMessage msg = messages.get(i);
			MessagePanel msgPanel = new MessagePanel(msg);
			if (!messagePanels.contains(msgPanel)) {
				addMessageHeader(msgPanel);
			}
		}	
	}
	
	public DialogPanelModel getModel() {
		return model;
	}
	
}
