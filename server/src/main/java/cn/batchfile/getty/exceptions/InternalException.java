package cn.batchfile.getty.exceptions;

public class InternalException extends RuntimeException {

	private static final long serialVersionUID = -8029071344285081830L;

	public InternalException() {
		super();
	}
	
	public InternalException(String message) {
		super(message);
	}
	
	public InternalException(String message, Throwable t) {
		super(message, t);
	}
	
	public InternalException(Throwable t) {
		super(t);
	}
}
