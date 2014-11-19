package cn.batchfile.getty.exceptions;

public class RewriteMappingException extends Exception {

	private static final long serialVersionUID = 6984922724589539151L;

	public RewriteMappingException() {
		super();
	}
	
	public RewriteMappingException(String message) {
		super(message);
	}

	public RewriteMappingException(String message, Throwable t) {
		super(message, t);
	}

	public RewriteMappingException(Throwable t) {
		super(t);
	}
}
