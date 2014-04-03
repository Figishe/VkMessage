package eltech.vkmessage.connection;

public class VkAuthorizationException extends VkNoConnectionExcepion {
	public VkAuthorizationException(String message) {
		super(message);
	}
	
	public VkAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}