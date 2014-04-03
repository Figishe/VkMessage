package eltech.vkmessage.connection;

public interface VkConnectionManager {
	public void connect(int appId, String login, String password) throws VkNoConnectionExcepion;
	public void reconnect() throws VkNoConnectionExcepion;
	public VkApiConnection getConnection() throws VkNoConnectionExcepion;
}
