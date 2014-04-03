package eltech.vkmessage.connection;

import java.io.IOException;

public class ConnectionManager implements VkConnectionManager {

	private VkApiConnection connection;

	private int connectionAttempts = 10;
	
	private String lastLogin;
	private String lastPasswpord;
	private int lastAppId;

	@Override
	public VkApiConnection getConnection() throws VkNoConnectionExcepion {
		if (connection != null) {
			return connection;
		} else {
			throw new VkNoConnectionExcepion("no estabilished connection found");
		}
	}
	
	public void connect(int appId, String login, String password) throws VkNoConnectionExcepion {
		if (login == null) {
			throw new IllegalArgumentException("login cant be null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password cant be null");
		}
		if (appId < 0) {
			throw new IllegalArgumentException("vk app id cant be negative");
		}
		
		lastLogin = login;
		lastPasswpord = password;
		lastAppId = appId;
		
		int attempt = 0;
		while (attempt <= connectionAttempts) {
			try {
				attempt++;
				this.connection = new VkApi.ConnectionBuilder(appId, login, password)
					.setScope("notify,friends,groups,photos,wall,offline,messages")
					.build();
				break;
			} catch (IOException e) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) { /* NOP */ }
			} 
		}
		if (connection == null) {
			throw new VkNoConnectionExcepion("no estabilished connection found");
		}
	}

	@Override
	public void reconnect() throws VkNoConnectionExcepion {
		if ((lastLogin == null) || (lastPasswpord == null)) {
			throw new AssertionError("Incorrect state: login = " + lastLogin + ", password = " + lastPasswpord + ". Have you called connect(...) before using reconnect()?" );
		} else {
			connect(lastAppId, lastLogin, lastPasswpord);
		}
	}

}
