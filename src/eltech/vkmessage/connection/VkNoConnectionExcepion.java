package eltech.vkmessage.connection;

public class VkNoConnectionExcepion extends VkException {

	public VkNoConnectionExcepion(String message, Throwable cause) {
		super(message, cause);
	}

	public VkNoConnectionExcepion(String message) {
		super(message);
	}

}
