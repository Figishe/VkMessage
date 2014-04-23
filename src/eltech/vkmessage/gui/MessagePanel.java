package eltech.vkmessage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import eltech.vkmessage.model.VkMessage;

public class MessagePanel extends JPanel {
	private final VkMessage message;
	
	private static final Color MSG_INCOMING_BACKGROUND = new Color(180, 200, 250);
	private static final Color MSG_OUTCOMING_BACKGROUND = new Color(220, 230, 250);

	public MessagePanel(VkMessage message) {
		super();
		this.message = message;
		this.setPreferredSize(new Dimension(300, 20));
	}

	public VkMessage getMessage() {
		return message;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		
		
		if (message.isIncoming()) {
			this.setBackground(MSG_INCOMING_BACKGROUND);
		} else {
			this.setBackground(MSG_OUTCOMING_BACKGROUND);
		}
		
		g.drawString(message.getBody(), 15, 15);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof MessagePanel) )
			return false;
		MessagePanel panel = (MessagePanel) obj;
		if (panel.message.equals(this.message)) 
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.message.hashCode();
	}
}
