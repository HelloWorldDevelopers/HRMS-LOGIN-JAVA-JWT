package hrms.rnt.login.customeException;

public class CustomeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4641719040659686856L;

	public CustomeException(String error) {
		super(error);
	}
	public CustomeException(Throwable error) {
		super(error);
	}

}
