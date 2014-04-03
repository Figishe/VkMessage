package eltech.vkmessage.main;

import eltech.vkmessage.connection.ConnectionManager;
import eltech.vkmessage.gui.AuthorizationFrame;
import eltech.vkmessage.gui.MessengerFrame;

public enum Application {
	
	INSTANCE;
	
	private ConnectionManager connManager;
	private MessengerFrame messengerFrame;
	
	private void onApplicationStarted() {
		connManager = new ConnectionManager();
		AuthorizationFrame authFrame = new AuthorizationFrame(connManager, "figishe@gmail.com");
	}
	
	public void onLoginSucceed() {
		this.messengerFrame = new MessengerFrame();
	}
	
	public static void main(String[] args) {
		INSTANCE.onApplicationStarted();
	}

	public ConnectionManager getConnectionManager() {
		return connManager;
	}

	public MessengerFrame getMessengerFrame() {
		return messengerFrame;
	}
}
