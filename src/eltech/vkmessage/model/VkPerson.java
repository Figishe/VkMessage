package eltech.vkmessage.model;

import org.json.JSONException;
import org.json.JSONObject;

import eltech.vkmessage.connection.VkApiMethodException;

public class VkPerson {
	
	private final int id;
	private final String firstName;
	private final String lastName;
	
	
	public VkPerson(JSONObject json) throws VkApiMethodException {
		try {
			this.id = json.getInt("id");
			this.firstName = json.getString("first_name");
			this.lastName = json.getString("last_name");
		} catch (JSONException e) {
			throw new VkApiMethodException("id, first_name or last_name are not set in JSON");
		}
	}


	public int getId() {
		return id;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}
}
