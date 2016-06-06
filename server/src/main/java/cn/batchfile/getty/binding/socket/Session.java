package cn.batchfile.getty.binding.socket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.SuspendToken;

import cn.batchfile.getty.exceptions.SuspendException;

public class Session {
	
	private org.eclipse.jetty.websocket.api.Session session;
	private String id;
	private long creationTime;
	private long lastAccessedTime;
	private Map<String, Object> attributes;
	private SuspendToken suspendToken;
	
	public Session(org.eclipse.jetty.websocket.api.Session session) {
		this.session = session;
		id = UUID.randomUUID().toString().replaceAll("-", "");
		creationTime = new Date().getTime();
		lastAccessedTime = creationTime;
		attributes = new HashMap<String, Object>();
	}
	
	public long getCreationTime() {
		return creationTime;
	}

	public String getId() {
		return id;
	}
	
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void close() {
		session.close();
	}
	
	public void close(int statusCode, String reason) {
		session.close(statusCode, reason);
	}
	
	public void setIdleTimeout(long ms) {
		session.setIdleTimeout(ms);
	}
	
	public long getIdleTimeout() {
		return session.getIdleTimeout();
	}
	
	public boolean isOpen() {
		return session.isOpen();
	}
	
	public boolean isSecure() {
		return session.isSecure();
	}
	
	public void suspend() {
		if (suspendToken != null) {
			throw new SuspendException("session is already suspended");
		}
		suspendToken = session.suspend();
	}
	
	public void resume() {
		suspendToken.resume();
		suspendToken = null;
	}
}
