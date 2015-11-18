package dad.makinito.hardware;

@SuppressWarnings("serial")
public class MakinitoException extends RuntimeException {

	public MakinitoException(String message, Throwable cause) {
		super(message, cause);
	}

	public MakinitoException(String message) {
		super(message);
	}
	
}
