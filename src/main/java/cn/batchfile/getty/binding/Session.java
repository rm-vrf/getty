package cn.batchfile.getty.binding;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Session {
	
	private HttpServletRequest request; 

	public Session(HttpServletRequest request) {
		this.request = request;
	}
	
	public String id() {
		return request.getRequestedSessionId();
	}

	public Session set(String name, Object value) {
		request.getSession(true).setAttribute(name, value);
		return this;
	}
	
	public Object get(String name) {
		return request.getSession(true).getAttribute(name);
	}
	
	public Map<String, Object> get() {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> names = request.getSession(true).getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = request.getSession().getAttribute(name);
			map.put(name, value);
		}
		return map;
	}
	
	public void remove(String name) {
		request.getSession(true).removeAttribute(name);
	}

	public void removeAll() {
		Enumeration<String> names = request.getSession(true).getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			request.getSession().removeAttribute(name);
		}
	}
}
