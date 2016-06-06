package cn.batchfile.getty.exceptions;

public class SuspendException extends RuntimeException {

	private static final long serialVersionUID = -5623694202810490527L;

	public SuspendException() {
		super();
	}
	
	public SuspendException(String message) {
		super(message);
	}

	public SuspendException(String message, Throwable t) {
		super(message, t);
	}

	public SuspendException(Throwable t) {
		super(t);
	}
}
