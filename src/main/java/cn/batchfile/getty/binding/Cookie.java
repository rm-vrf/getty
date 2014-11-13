package cn.batchfile.getty.binding;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class Cookie {

	private HttpServletRequest request; 
	private HttpServletResponse response;
	
	public Cookie(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public String get(String name) {
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		for (javax.servlet.http.Cookie cookie : cookies) {
			if (StringUtils.equals(cookie.getName(), name)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public Map<String, String> get() {
		Map<String, String> map = new HashMap<String, String>();
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		for (javax.servlet.http.Cookie cookie : cookies) {
			map.put(cookie.getName(), cookie.getValue());
		}
		return map;
	}
	
	public Cookie put(String name, String value, String domain, String path, int maxAge, boolean secure) {
		javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, value);
		if (!StringUtils.isEmpty(domain)) {
			cookie.setDomain(domain);
		}
		if (!StringUtils.isEmpty(path)) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(maxAge);
		cookie.setSecure(secure);
		
		response.addCookie(cookie);
		return this;
	}
	
	public Cookie put(String name, String value, int maxAge) {
		return put(name, value, null, null, maxAge, false);
	}
	
	public Cookie put(String name, String value) {
		return put(name, value, null, null, -1, false);
	}
	
	public Cookie remove(String name) {
		return put(name, StringUtils.EMPTY, 0);
	}
	
	public void removeAll() {
		Map<String, String> map = get();
		for (String key : map.keySet()) {
			remove(key);
		}
	}
}
