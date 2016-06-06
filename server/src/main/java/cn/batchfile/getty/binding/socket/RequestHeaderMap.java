package cn.batchfile.getty.binding.socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestHeaderMap implements Map<String, Object> {
	
	private Map<String, List<String>> headers;
	
	public RequestHeaderMap(Map<String, List<String>> headers) {
		this.headers = headers;
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
		return map().get(key);
	}

	@Override
	public Object put(String key, Object value) {
		List<String> list = new ArrayList<String>();
		list.add(value == null ? null : value.toString());
		return headers.put(key, list);
	}

	@Override
	public Object remove(Object key) {
		//List<String> list = new ArrayList<String>();
		//list.add(value == null ? null : value.toString());
		return headers.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (java.util.Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			List<String> list = new ArrayList<String>();
			list.add(entry.getValue() == null ? null : entry.getValue().toString());
			headers.put(entry.getKey(), list);
		}
	}

	@Override
	public void clear() {
		headers.clear();
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
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			List<String> list = entry.getValue();
			if (list != null && list.size() == 1) {
				map.put(entry.getKey(), list.get(0));
			} else if (list != null && list.size() == 0) {
				map.put(entry.getKey(), null);
			} else {
				map.put(entry.getKey(), list);
			}
		}
		return map;
	}
}
