package cn.batchfile.getty.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class RequestAttributeMap implements Map<String, Object> {

	private HttpServletRequest request;
	
	public RequestAttributeMap(HttpServletRequest request) {
		this.request = request;
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
	public boolean containsKey(Object name) {
		return map().containsKey(name);
	}

	@Override
	public boolean containsValue(Object o) {
		return map().containsValue(o);
	}

	@Override
	public Object get(Object name) {
		return map().get(name);
	}

	@Override
	public Object put(String name, Object o) {
		request.setAttribute(name, o);
		return o;
	}

	@Override
	public Object remove(Object name) {
		Object o = request.getAttribute(name.toString());
		request.removeAttribute(name.toString());
		return o;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> objects) {
		for (Entry<? extends String, ? extends Object> entry : objects.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		Enumeration<String> names = request.getAttributeNames();
		List<String> list = new ArrayList<String>();
		while (names.hasMoreElements()) {
			list.add(names.nextElement());
		}
		for (String name : list) {
			request.removeAttribute(name);
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

	public Map<String, Object> map() {
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
