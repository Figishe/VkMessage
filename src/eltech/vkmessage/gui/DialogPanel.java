package eltech.vkmessage.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

	private DialogMessagesPanel msgPanel;
	
	public DialogPanel(VkDialog dialog) {
		super();
		this.msgPanel = new DialogMessagesPanel(new DialogPanelModel(dialog));
		this.setLayout(new BorderLayout());
		
		// adding UI components - messages panel and input component for writing new messages
		// TODO: add to jscrollPane
		this.add(msgPanel, BorderLayout.CENTER);
		this.add(new NewMessagePanel(this), BorderLayout.SOUTH);
		
		onPanelOpened();
	}
	
	private void onPanelOpened() {
		msgPanel.update();
		this.repaint();
		this.updateUI();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
