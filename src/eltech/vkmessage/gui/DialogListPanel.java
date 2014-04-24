package eltech.vkmessage.gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;
import eltech.vkmessage.model.VkDialog;
import eltech.vkmessage.model.VkPerson;

public class DialogListPanel extends JPanel {
	private List<DialogHeaderPanel> dialogHeaders;
	private Thread updaterThread;
	
	public DialogListPanel() {
		dialogHeaders = new LinkedList<DialogHeaderPanel>();
		this.setLayout(new GridLayout(0, 1));
		
		Runnable updaterLogic = new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						// NOP
					}
					update();
				}
			}
		};
		
		this.updaterThread = new Thread(updaterLogic);
		updaterThread.setDaemon(true);
		updaterThread.start();
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
			
			// removing all currently existing panels info 
			dialogHeaders.clear();
			this.removeAll();
			
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
		
		
		// ******
		// This ugly part of code uploads user names for naming dialogs that are not chats (only 2 persons)
		// ******
		
		Map<Integer, DialogHeaderPanel> uiHeadersMap = new HashMap<Integer, DialogHeaderPanel>();
		List<Integer> userUids = new LinkedList<Integer>();
		for (DialogHeaderPanel header : dialogHeaders) {
			if (header.getDialog().isChat() == false) {
				int partnerId = header.getDialog().getUserId();
				userUids.add(partnerId);
				uiHeadersMap.put(partnerId, header);
			}
		}
		try {
			// building string with ids of users 
			StringBuilder uids = new StringBuilder();
			for (Integer uid : userUids) {
				uids.append(uid);
				uids.append(",");
			}
			if (! (uids.length() <= 0) ) {
				uids.deleteCharAt(uids.length() - 1); // removes last ','
			}
			
			//getting users via api
			JSONObject response = Application.INSTANCE.getConnectionManager().getConnection().
					executeApiMethod("users.get", "user_ids="  + uids.toString());
			JSONArray usersArray = response.getJSONArray("response");
			for (int i = 0; i < usersArray.length(); i++) {
				VkPerson person = new VkPerson(usersArray.getJSONObject(0));
				String uiHeader = person.getFirstName() + " " + person.getLastName(); 
				
				// @HACK
				// we depend on fact that jsonArray order == userUids order, and their sizes are equal
				// Or not?
				
				DialogHeaderPanel currentPanel = uiHeadersMap.get(person.getId());
				if (currentPanel != null) {
					currentPanel.setUiTitle(uiHeader);
				}
			}
		} catch (VkApiMethodException e) {
			e.printStackTrace();
		} catch (VkNoConnectionExcepion e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// ******
		// ******
		
		for (DialogHeaderPanel header : dialogHeaders) {
			this.add(header);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
