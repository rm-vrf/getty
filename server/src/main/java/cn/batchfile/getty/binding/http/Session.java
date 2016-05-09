package cn.batchfile.getty.binding.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class Session {

	private HttpServletRequest request;
	private SessionAttributeMap attributes;

	public Session(HttpServletRequest request) {
		this.request = request;
		attributes = new SessionAttributeMap(request);
	}
	
	public long getCreationTime() {
		return request.getSession(true).getCreationTime();
	}
	
	public String getId() {
		return request.getSession(true).getId();
	}
	
	public long getLastAccessedTime() {
		return request.getSession(true).getLastAccessedTime();
	}
	
	public int getMaxInactiveInterval() {
		return request.getSession(true).getMaxInactiveInterval();
	}
	
	public Session setMaxInactiveInterval(int maxInactiveInterval) {
		request.getSession(true).setMaxInactiveInterval(maxInactiveInterval);
		return this;
	}
	
	public ServletContext getServletContext() {
		return request.getSession(true).getServletContext();
	}
	
	public SessionAttributeMap getAttributes() {
		return attributes;
	}
}
