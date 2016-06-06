package cn.batchfile.getty.binding.socket;

import java.net.HttpCookie;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cookie implements Map<String, HttpCookie> {
	
	private List<HttpCookie> cookies;
	
	public Cookie(List<HttpCookie> cookies) {
		this.cookies = cookies;
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
	public HttpCookie get(Object key) {
		return map().get(key);
	}

	@Override
	public HttpCookie put(String key, HttpCookie value) {
		cookies.add(value);
		//return map().put(key, value);
		return value;
	}

	@Override
	public HttpCookie remove(Object key) {
		cookies.remove(key);
		return map().remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends HttpCookie> m) {
		for (HttpCookie cookie : m.values()) {
			cookies.add(cookie);
		}
		//map().putAll(m);
	}

	@Override
	public void clear() {
		cookies.clear();
		//map().clear();
	}

	@Override
	public Set<String> keySet() {
		return map().keySet();
	}

	@Override
	public Collection<HttpCookie> values() {
		return map().values();
	}

	@Override
	public Set<java.util.Map.Entry<String, HttpCookie>> entrySet() {
		return map().entrySet();
	}

	private Map<String, HttpCookie> map() {
		Map<String, HttpCookie> map = new HashMap<String, HttpCookie>();
		for (HttpCookie cookie : cookies) {
			map.put(cookie.getName(), cookie);
		}
		return map;
	}
}
