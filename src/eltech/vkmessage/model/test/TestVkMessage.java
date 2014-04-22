package eltech.vkmessage.model.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

import eltech.vkmessage.connection.VkApiMethodException;
import eltech.vkmessage.model.VkMessage;

public class TestVkMessage {
	
	@Test(expected = VkApiMethodException.class) 
	public void testIncorrectMessageJsonShouldFail() {
		try {
			VkMessage msg = new VkMessage(new JSONObject());
		} catch (VkApiMethodException e) {
		}
	}
	
	@Test 
	public void testMessageIsParsedCorrectlyFromJson() {
		String json = "response: {id: 552885,date: 1376054836,out: 0," +
				"user_id: 185014513,read_state: 0,title: ' ... ',body: 'test'}";
		// creating json based on string
		JSONObject msgJson = null;
		try {
			msgJson = new JSONObject(json);
		} catch (JSONException e1) {
			fail();
		}
		
		
		// checking if created vkMessage object is in valid state
		try {
			VkMessage msg;
			try {
				msg = new VkMessage(msgJson.getJSONObject("response"));
				assertEquals(" ... ", msg.getTitle());
				assertEquals("test", msg.getBody());
			} catch (JSONException e) {
				fail();
			}

		} catch (VkApiMethodException e) {
			fail("got exception while creating legal message");
		}
	}
}
