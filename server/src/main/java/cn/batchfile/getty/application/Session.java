package cn.batchfile.getty.application;

import java.util.List;

public class Session {

	private int timeout = 0;
	private List<SessionListener> listeners;

	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public List<SessionListener> getListeners() {
		return listeners;
	}
	
	public void setListeners(List<SessionListener> listeners) {
		this.listeners = listeners;
	}
}
