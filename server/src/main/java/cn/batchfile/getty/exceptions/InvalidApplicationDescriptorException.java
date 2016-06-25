package cn.batchfile.getty.exceptions;

public class InvalidApplicationDescriptorException extends RuntimeException {

	private static final long serialVersionUID = 8389266317741821834L;

	public InvalidApplicationDescriptorException() {
		super();
	}
	
	public InvalidApplicationDescriptorException(String message) {
		super(message);
	}
	
	public InvalidApplicationDescriptorException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidApplicationDescriptorException(Throwable t) {
		super(t);
	}
}
