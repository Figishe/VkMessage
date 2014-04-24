package eltech.vkmessage.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkConnectionManager;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;
import eltech.vkmessage.model.DialogPanelModel;
import eltech.vkmessage.model.VkDialog;
import eltech.vkmessage.model.VkPerson;



public class DialogHeaderPanel extends JPanel implements MouseListener {
	private VkDialog dialog;
	private String uiTitle;
	
	public DialogHeaderPanel(VkDialog dialog) {
		this.dialog = dialog;
		
		setUiTitle(dialog.getLastMessage().getTitle());
			
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(300, 80));
		
	}
	
	public VkDialog getDialog() {
		return dialog;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString(this.uiTitle, 5, 10);
		g.drawString(dialog.getLastMessage().getBody(), 5, 30);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		DialogPanel dialogPanel = new DialogPanel(this.dialog);
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
	public void setUiTitle(String title) {
		this.uiTitle = title;
	}
	
	@Deprecated
	private String getDialogTitleByUserId(int userId) {
		try {
			JSONObject response = Application.INSTANCE.getConnectionManager().getConnection().executeApiMethod("users.get", "user_ids="  + userId);
			VkPerson person = new VkPerson(response.getJSONArray("response").getJSONObject(0));
			return person.getFirstName() + " " + person.getLastName(); 
		} catch (VkApiMethodException e) {
			e.printStackTrace();
		} catch (VkNoConnectionExcepion e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "Little Gnome";
	}
}
