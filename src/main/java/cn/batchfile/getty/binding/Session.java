package cn.batchfile.getty.binding;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Session {

	private HttpSession session;
	private SessionAttributeMap attributes;

	public Session(HttpServletRequest request) {
		session = request.getSession(true);
		attributes = new SessionAttributeMap(session);
	}
	
	public long getCreationTime() {
		return session.getCreationTime();
	}
	
	public String getId() {
		return session.getId();
	}
	
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}
	
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}
	
	public Session setMaxInactiveInterval(int maxInactiveInterval) {
		session.setMaxInactiveInterval(maxInactiveInterval);
		return this;
	}
	
	public ServletContext getServletContext() {
		return session.getServletContext();
	}
	
	public SessionAttributeMap getAttributes() {
		return attributes;
	}
}
