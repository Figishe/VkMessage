package eltech.vkmessage.connection.test;

import org.junit.Test;

import eltech.vkmessage.connection.ConnectionManager;
import eltech.vkmessage.connection.VkNoConnectionExcepion;

import static org.junit.Assert.*;

public class TestConnectionManager {
	private ConnectionManager manager = new ConnectionManager();  
	
	public TestConnectionManager() {
		super();
	}
	
	@Test
	public void TestIncorrectAuthorization() {
		try {
			manager.connect(1, "figishe@gmail.com", "wrong password");
		} catch (VkNoConnectionExcepion e) {
			return;
		}
		fail("no connection exception got with incorrect login + pass");
	}
}
