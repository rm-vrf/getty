package cn.batchfile.getty.application;

public class WebSocketHandler {
	private String url;
	private String connect;
	private String close;
	private String message;
	private String error;
    private long maxIdleTime;
    private String batchMode; 
    private int inputBufferSize;
    private int maxBinaryMessageSize;
    private int maxTextMessageSize;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public long getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public String getBatchMode() {
		return batchMode;
	}

	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}

	public int getInputBufferSize() {
		return inputBufferSize;
	}

	public void setInputBufferSize(int inputBufferSize) {
		this.inputBufferSize = inputBufferSize;
	}

	public int getMaxBinaryMessageSize() {
		return maxBinaryMessageSize;
	}

	public void setMaxBinaryMessageSize(int maxBinaryMessageSize) {
		this.maxBinaryMessageSize = maxBinaryMessageSize;
	}

	public int getMaxTextMessageSize() {
		return maxTextMessageSize;
	}

	public void setMaxTextMessageSize(int maxTextMessageSize) {
		this.maxTextMessageSize = maxTextMessageSize;
	}
}
