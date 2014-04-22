package eltech.vkmessage.gui;

import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

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
		this.messagePanels = new LinkedList<MessagePanel>();
	}
	
	private void addMessage(VkMessage message) {
		messagePanels.add(new MessagePanel(message));
	}
	
	public void update() {
		model.updateMessages();
		
		// todo: reverse add
		for (VkMessage msg : model.getMessages())
			addMessage(msg);
	}
	
	public DialogPanelModel getModel() {
		return model;
	}
	
}
