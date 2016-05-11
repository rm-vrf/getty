package cn.batchfile.getty.binding.socket;

import java.util.Map;

public class Session {

	public String getId() {
		return null;
	}
	
	public Map<String, Object> getAttributes() {
		return null;
	}
	
	public void close() {
		//TODO
	}
	
	public void close(int statusCode, String reason) {
		//TODO
	}
	
	public void setIdleTimeout(long ms) {
		//TODO
	}
	
	public long getIdleTimeout() {
		return 0;
	}
	
	public boolean isOpen() {
		return false;
	}
	
	public boolean isSecure() {
		return false;
	}
	
	public void suspend() {
		//TODO
	}
	
	public void resume() {
		//TODO
	}
}
