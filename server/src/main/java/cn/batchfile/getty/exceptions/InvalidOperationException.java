package cn.batchfile.getty.exceptions;

public class InvalidOperationException extends RuntimeException {

	private static final long serialVersionUID = 6401239119286112864L;

	public InvalidOperationException() {
		super();
	}
	
	public InvalidOperationException(String message) {
		super(message);
	}
	
	public InvalidOperationException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidOperationException(Throwable t) {
		super(t);
	}
 }
