package eltech.vkmessage.connection;

public class VkApiMethodException extends VkException {
	protected final int errorCode;
	protected final String requestParams;
	
	public int getErrorCode() {
		return errorCode;
	}

	public String getRequestParams() {
		return requestParams;
	}

	VkApiMethodException(int errorCode, String message,
			String requestParams) {
		super("Error " + errorCode + ": " + message);
		this.errorCode = errorCode;
		this.requestParams = requestParams;
	}
	
	public VkApiMethodException(int errorCode, String message,
			String requestParams, Throwable cause) {
		super("Error " + errorCode + ": " + message, cause);
		this.errorCode = errorCode;
		this.requestParams = requestParams;
		
	}
	 public VkApiMethodException(String message) {
		 super(message);
		 errorCode = -1;
		 requestParams = null;
	}
	 
	 public VkApiMethodException(String message, Throwable cause) {
		 super(message, cause);
		 errorCode = -1;
		 requestParams = null;
	}
	
}
