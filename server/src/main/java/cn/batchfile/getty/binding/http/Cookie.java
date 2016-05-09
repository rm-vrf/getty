package cn.batchfile.getty.binding.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class Cookie implements Map<String, javax.servlet.http.Cookie> {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public Cookie(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	private Map<String, javax.servlet.http.Cookie> map() {
		Map<String, javax.servlet.http.Cookie> map = new HashMap<String, javax.servlet.http.Cookie>();
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (javax.servlet.http.Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie);
			}
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
	public boolean containsKey(Object name) {
		return map().containsKey(name);
	}

	@Override
	public boolean containsValue(Object value) {
		return map().containsValue(value);
	}

	@Override
	public javax.servlet.http.Cookie get(Object name) {
		return map().get(name);
	}

	@Override
	public javax.servlet.http.Cookie put(String name,
			javax.servlet.http.Cookie cookie) {
		response.addCookie(cookie);
		return cookie;
	}
	
	public javax.servlet.http.Cookie put(javax.servlet.http.Cookie cookie) {
		response.addCookie(cookie);
		return cookie;
	}
	
	public javax.servlet.http.Cookie put(String name, String value) {
		return put(name, value, null, null, -1, false);
	}
	
	public javax.servlet.http.Cookie put(String name, String value, int maxAge) {
		return put(name, value, null, null, maxAge, false);
	}
	
	public javax.servlet.http.Cookie put(String name, String value, String domain, String path, int maxAge, boolean secure) {
		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, value);
		
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
	public javax.servlet.http.Cookie remove(Object name) {
		javax.servlet.http.Cookie cookie = get(name);
		put(name.toString(), StringUtils.EMPTY, 0);
		return cookie;
	}

	@Override
	public void putAll(
			Map<? extends String, ? extends javax.servlet.http.Cookie> m) {
		for (Entry<? extends String, ? extends javax.servlet.http.Cookie> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		Map<String, javax.servlet.http.Cookie> map = map();
		for (String key : map.keySet()) {
			remove(key);
		}
	}

	@Override
	public Set<String> keySet() {
		return map().keySet();
	}

	@Override
	public Collection<javax.servlet.http.Cookie> values() {
		return map().values();
	}

	@Override
	public Set<java.util.Map.Entry<String, javax.servlet.http.Cookie>> entrySet() {
		return map().entrySet();
	}
}
