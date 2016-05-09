package cn.batchfile.getty.binding.http;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionAttributeMap implements Map<String, Object>{

	private HttpServletRequest request;
	
	public SessionAttributeMap(HttpServletRequest request) {
		this.request = request;
	}

	private Map<String, Object> map() {
		HttpSession session = request.getSession(true);
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = session.getAttribute(name);
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
		return request.getSession(true).getAttribute(key.toString());
	}

	@Override
	public Object put(String key, Object value) {
		request.getSession(true).setAttribute(key, value);
		return value;
	}

	@Override
	public Object remove(Object key) {
		Object r = request.getSession(true).getAttribute(key.toString());
		request.getSession(true).removeAttribute(key.toString());
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
		HttpSession session = request.getSession(true);
		Enumeration<String> names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			session.removeAttribute(name);
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
