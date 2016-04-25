package cn.batchfile.getty.binding;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

public class ResponseHeaderMap implements Map<String, Object> {
	
	private HttpServletResponse servletResponse;
	
	public ResponseHeaderMap(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
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
	public boolean containsValue(Object value) {
		return map().containsValue(value);
	}

	@Override
	public Object get(Object name) {
		return map().get(name);
	}

	@Override
	public Object put(String name, Object value) {
		if (value instanceof Date) {
			servletResponse.addDateHeader(name, ((Date)value).getTime());
		} else if (value instanceof Integer) {
			servletResponse.addIntHeader(name, (Integer)value);
		} else {
			servletResponse.addHeader(name, value.toString());
		}
		return value;
	}

	@Override
	public Object remove(Object name) {
		Object value = get(name);
		servletResponse.addHeader(name.toString(), null);
		return value;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> values) {
		for (Entry<? extends String, ? extends Object> entry : values.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		for (String key : keySet()) {
			remove(key);
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
	
	private Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>();
		Collection<String> names = servletResponse.getHeaderNames();
		for (String name : names) {
			Object value = servletResponse.getHeader(name);
			map.put(name, value);
		}
		return map;
	}
}
