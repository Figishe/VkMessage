package eltech.vkmessage.model;

import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.connection.VkNoConnectionExcepion;
import eltech.vkmessage.main.Application;

public class VkMessage {
	private final int id;
	private final int userId;
	private final int chatId;

	private final int out;

	private final String title;
	private final String body;

	public VkMessage(JSONObject jsonObj) throws VkApiMethodException {
		try {
			if (jsonObj.has("id"))
				this.id = jsonObj.getInt("id");
			else
				this.id = -1;
		} catch (JSONException e) {
			throw new VkApiMethodException("message has no id", e);
		}
		try {
			this.userId = jsonObj.getInt("user_id");
		} catch (JSONException e) {
			throw new VkApiMethodException("message has no user_id", e);
		}
		try {
			if (jsonObj.has("chat_id"))
				this.chatId = jsonObj.getInt("chat_id");
			else
				this.chatId = -1;
		} catch (JSONException e) {
			throw new VkApiMethodException("message has no chat_id", e);
		}

		VkPerson person;
		String title = " ... ";
//		try {
//			person = new VkPerson(Application.INSTANCE.getConnectionManager()
//					.getConnection()
//					.executeApiMethod("users.get", "id=" + userId).getJSONArray("response").getJSONObject(0));
//			title = person.getFirstName() + " " + person.getLastName();
//		} catch (VkNoConnectionExcepion e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		this.title = title;

		try {
			this.body = jsonObj.getString("body");
		} catch (JSONException e) {
			throw new VkApiMethodException("message has no body", e);
		}
		try {
			this.out = jsonObj.getInt("out");
		} catch (JSONException e) {
			throw new VkApiMethodException("message has no out flag", e);
		}
	}

	public int getId() {
		return id;
	}

	public int getChatId() {
		return chatId;
	}

	public int getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public boolean isIncoming() {
		return (out == 0);
	}

	public boolean isOutcoming() {
		return !isIncoming();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof VkMessage))
			return false;
		VkMessage msg = (VkMessage) obj;
		return (msg.body.equals(this.body)) && (msg.id == this.id) && (msg.userId == this.userId);
	}
	
	@Override
	public int hashCode() {
		return body.hashCode();
	}
}
