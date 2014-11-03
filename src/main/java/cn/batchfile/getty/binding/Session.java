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

	public Session set(String name, Object value) {
		request.getSession(true).setAttribute(name, value);
		return this;
	}
	
	public Object get(String name) {
		return request.getSession(true).getAttribute(name);
	}
	
	public Map<String, Object> get() {
		Map<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getSession(true).getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = request.getSession().getAttribute(name);
			map.put(name, value);
		}
		return map;
	}
}
