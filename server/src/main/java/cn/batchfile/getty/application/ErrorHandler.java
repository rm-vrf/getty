package cn.batchfile.getty.application;

public class ErrorHandler {

	private int errorCode = 0;
	private String file;
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
}
