package cn.batchfile.getty.binding.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Session {

	private HttpSession httpSession;
	private HttpServletRequest request;
	private SessionAttributeMap attributes;
	
	public Session(HttpSession httpSession) {
		this.httpSession = httpSession;
		attributes = new SessionAttributeMap(httpSession);
	}

	public Session(HttpServletRequest request) {
		this.request = request;
		attributes = new SessionAttributeMap(request);
	}
	
	public long getCreationTime() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getCreationTime();
	}
	
	public String getId() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getId();
	}
	
	public long getLastAccessedTime() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getLastAccessedTime();
	}
	
	public int getMaxInactiveInterval() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getMaxInactiveInterval();
	}
	
	public Session setMaxInactiveInterval(int maxInactiveInterval) {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		httpSession.setMaxInactiveInterval(maxInactiveInterval);
		return this;
	}
	
	public ServletContext getServletContext() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getServletContext();
	}
	
	public SessionAttributeMap getAttributes() {
		return attributes;
	}
}
