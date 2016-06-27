package cn.batchfile.getty.binding.socket;

import java.net.HttpCookie;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
	public HttpCookie get(Object name) {
		return map().get(name);
	}

	@Override
	public HttpCookie put(String name, HttpCookie value) {
		cookies.add(value);
		return value;
	}
	
	public HttpCookie put(HttpCookie cookie) {
		cookies.add(cookie);
		return cookie;
	}
	
	public HttpCookie put(String name, String value) {
		return put(name, value, null, null, -1, false);
	}
	
	public HttpCookie put(String name, String value, int maxAge) {
		return put(name, value, null, null, maxAge, false);
	}
	
	public HttpCookie put(String name, String value, String domain, String path, int maxAge, boolean secure) {
		HttpCookie cookie = new HttpCookie(name, value);
		
		if (!StringUtils.isEmpty(domain)) {
			cookie.setDomain(domain);
		}
		
		if (StringUtils.isEmpty(path)) {
			cookie.setPath("/");
		} else {
			cookie.setPath(path);
		}

		cookie.setMaxAge(maxAge);
		cookie.setSecure(secure);
		
		return put(cookie);
	}
	
	@Override
	public HttpCookie remove(Object key) {
		HttpCookie cookie = map().get(key);
		cookies.remove(cookie);
		return cookie;
	}

	@Override
	public void putAll(Map<? extends String, ? extends HttpCookie> m) {
		for (HttpCookie cookie : m.values()) {
			cookies.add(cookie);
		}
	}

	@Override
	public void clear() {
		cookies.clear();
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
