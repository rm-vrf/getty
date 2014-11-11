package cn.batchfile.getty.binding;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class Model {

	private HttpServletRequest request;
	
	public Model(HttpServletRequest request) {
		this.request = request;
	}

	public Model put(String name, Object object) {
		request.setAttribute(name, object);
		return this;
	}
	
	public Model put(Map<String, Object> objects) {
		for (Entry<String, Object> entry : objects.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public Object get(String name) {
		return request.getAttribute(name);
	}
	
	public Map<String, Object> get() {
		Enumeration<String> names = request.getAttributeNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object object = request.getAttribute(name);
			map.put(name, object);
		}
		return map;
	}
}
