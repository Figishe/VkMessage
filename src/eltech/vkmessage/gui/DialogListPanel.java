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
import eltech.vkmessage.model.VkDialog;

public class DialogListPanel extends JPanel {
	private List<DialogHeaderPanel> dialogHeaders;
	
	public DialogListPanel() {
		dialogHeaders = new LinkedList<DialogHeaderPanel>();
		this.setLayout(new GridLayout(0, 1));
	}
	
	public void update() {
		try {
			JSONObject response;
			response = Application.INSTANCE.getConnectionManager().getConnection()
					.executeApiMethod("messages.getDialogs", "count=20").getJSONObject("response");
			JSONArray dialogsJsonArray;
			try {
				dialogsJsonArray = response.getJSONArray("items");
			} catch (JSONException e1) {
				// No dialogs to fill the list
				e1.printStackTrace();
				return;
			}
			for (int index = 0; index < dialogsJsonArray.length(); index++) {
				try {
					JSONObject dialogJson = dialogsJsonArray.getJSONObject(index);
					VkDialog dialog = new VkDialog(dialogJson);
					dialogHeaders.add(new DialogHeaderPanel(dialog));
				} catch (JSONException e) {
				}
			}
		} catch (VkApiMethodException e) {
			e.printStackTrace();
		} catch (VkNoConnectionExcepion e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		for (DialogHeaderPanel header : dialogHeaders) {
			this.add(header);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
