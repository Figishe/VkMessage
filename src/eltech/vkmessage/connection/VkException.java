package eltech.vkmessage.connection;

public class VkException extends Exception {
	public VkException(String message) {
		super(message);
	}

	public VkException(String message, Throwable cause) {
		super(message, cause);
	}
}
