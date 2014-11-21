package cn.batchfile.getty.binding;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class SessionAttributeMap implements Map<String, Object>{

	private HttpSession session;
	
	public SessionAttributeMap(HttpSession session) {
		this.session = session;
	}

	private Map<String, Object> map() {
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
		return session.getAttribute(key.toString());
	}

	@Override
	public Object put(String key, Object value) {
		session.setAttribute(key, value);
		return value;
	}

	@Override
	public Object remove(Object key) {
		Object r = session.getAttribute(key.toString());
		session.removeAttribute(key.toString());
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
