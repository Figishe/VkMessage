package eltech.vkmessage.gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;
import eltech.vkmessage.model.DialogPanelModel;
import eltech.vkmessage.model.VkDialog;
import eltech.vkmessage.model.VkMessage;

public class DialogPanel extends JPanel {
	
	private DialogPanelModel model;
	private List<MessagePanel> messagePanels;
	

	public DialogPanel(DialogPanelModel model) {
		super();
		this.model = model;
		this.setLayout(new GridLayout(0, 1));
		this.messagePanels = new LinkedList<MessagePanel>();
		onPanelOpened();
	}
	
	public DialogPanelModel getModel() {
		return model;
	}

	public void update() {
		
	}
	
	private void onPanelOpened() {
		this.add(new NewMessagePanel(this));
		model.uploadDefaultMessages();
		List<VkMessage> messages = model.getMessages();
		for (VkMessage msg : messages) {
			this.add(new MessagePanel(msg));
		}
		this.repaint();
		this.updateUI();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
