package eltech.vkmessage.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import eltech.vkmessage.main.Application;
import eltech.vkmessage.model.DialogPanelModel;
import eltech.vkmessage.model.VkDialog;



public class DialogHeaderPanel extends JPanel implements MouseListener {
	private VkDialog dialog;
	
	public DialogHeaderPanel(VkDialog dialog) {
		this.dialog = dialog;
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(300, 80));
	}
	
	public VkDialog getDialog() {
		return dialog;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString(dialog.getLastMessage().getTitle(), 5, 10);
		g.drawString(dialog.getLastMessage().getBody(), 5, 30);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		DialogPanelModel model = new DialogPanelModel(this.dialog);
		DialogPanel dialogPanel = new DialogPanel(model);
		Application.INSTANCE.getMessengerFrame().setActiveDialogPanel(dialogPanel);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
