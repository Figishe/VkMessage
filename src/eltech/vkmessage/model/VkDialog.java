package eltech.vkmessage.model;

import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;

public class VkDialog {
	private VkMessage lastMessage;
	private int unreadMessagesCount;
	
	public VkDialog(JSONObject jsonObj) throws VkApiMethodException {
		//jsonObj 
		try {
			try {
				this.lastMessage = new VkMessage(jsonObj.getJSONObject("message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (jsonObj.has("unread")) { 
				try {
					this.unreadMessagesCount = jsonObj.getInt("unread");
				} catch (JSONException e) {
					// NOP
				}
			} else {
				this.unreadMessagesCount = 0;
			}	
		} catch (VkApiMethodException e) {
			throw new VkApiMethodException("Can't create dialog...", e);
		}
	}
	
	public int getChatId() {
		if (lastMessage.getChatId() != -1)
			return lastMessage.getChatId();
		throw new IllegalStateException("This dialog is not chat, can't get chatId");
	}
	
	public int getUserId() {
		return lastMessage.getUserId();
	}
	
	public boolean isChat() {
		return lastMessage.getChatId() != -1;
	}

	public VkMessage getLastMessage() {
		return lastMessage;
	}

	public int getUnreadMessagesCount() {
		return unreadMessagesCount;
	}
}
