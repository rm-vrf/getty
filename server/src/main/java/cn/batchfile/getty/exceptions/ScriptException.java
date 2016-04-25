package cn.batchfile.getty.exceptions;

public class ScriptException extends RuntimeException {

	private static final long serialVersionUID = -6917591847710229411L;

	public ScriptException() {
		super();
	}
	
	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(String message, Throwable t) {
		super(message, t);
	}

	public ScriptException(Throwable t) {
		super(t);
	}
}
