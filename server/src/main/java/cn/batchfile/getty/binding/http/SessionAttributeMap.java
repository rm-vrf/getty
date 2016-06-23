package cn.batchfile.getty.binding.http;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionAttributeMap implements Map<String, Object>{

	private HttpSession httpSession;
	private HttpServletRequest request;
	
	public SessionAttributeMap(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	public SessionAttributeMap(HttpServletRequest request) {
		this.request = request;
	}

	private Map<String, Object> map() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> names = httpSession.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = httpSession.getAttribute(name);
			map.put(name, value);
		}
		return map;
	}

	@Override
	public int size() {
		return map().size();
	}

	@Override
	public boolean isEmpty() {
		return map().isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map().containsValue(value);
	}

	@Override
	public Object get(Object key) {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		return httpSession.getAttribute(key.toString());
	}

	@Override
	public Object put(String key, Object value) {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		httpSession.setAttribute(key, value);
		return value;
	}

	@Override
	public Object remove(Object key) {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		Object r = httpSession.getAttribute(key.toString());
		httpSession.removeAttribute(key.toString());
		return r;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		if (httpSession == null) {
			httpSession = request.getSession(true);
		}
		Enumeration<String> names = httpSession.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			httpSession.removeAttribute(name);
		}
	}

	@Override
	public Set<String> keySet() {
		return map().keySet();
	}

	@Override
	public Collection<Object> values() {
		return map().values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map().entrySet();
	}
}
