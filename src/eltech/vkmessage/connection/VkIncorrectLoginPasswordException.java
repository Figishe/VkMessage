package eltech.vkmessage.connection;

public class VkIncorrectLoginPasswordException extends VkAuthorizationException {

	public VkIncorrectLoginPasswordException(String message) {
		super(message);
	}

	public VkIncorrectLoginPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
