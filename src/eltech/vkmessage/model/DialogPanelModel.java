package eltech.vkmessage.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;

/**
 * DialogPanelModel represents the model of dialog shown on gui, 
 * with some actual messages uploaded
 *
 */

public class DialogPanelModel {
	private VkDialog dialog;
	private List<VkMessage> messages;
	
	private final int MESSAGES_AMOUNT_DEFAULT = 15;

	public DialogPanelModel(VkDialog dialog) {
		this.dialog = dialog;
		this.messages = new LinkedList<VkMessage>();
	}

	public List<VkMessage> getMessages() {
		return messages;
	}

	public VkDialog getDialog() {
		return dialog;
	}

	public void updateMessages() {
		this.messages.clear();
		uploadDefaultMessages();
	}
	
	
	
	public void uploadDefaultMessages() {
		String methodArgs = "count=" + MESSAGES_AMOUNT_DEFAULT;
		if (dialog.isChat()) {
			methodArgs += "&chat_id=" + dialog.getChatId();
		} else {
			methodArgs += "&user_id=" + dialog.getUserId();
		}
		methodArgs += "&rev=0";
		try {
			JSONObject response = Application.INSTANCE.getConnectionManager().getConnection().executeApiMethod("messages.getHistory", methodArgs);
			try {
				JSONArray messages = response.getJSONObject("response").getJSONArray("items");
				for (int i = 0; i < messages.length(); i++) {
					VkMessage message = new VkMessage(messages.getJSONObject(i));
					this.messages.add(message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (VkApiMethodException e) {
			e.printStackTrace();
		} catch (VkNoConnectionExcepion e) {
			e.printStackTrace();
		}
		
	}
}
