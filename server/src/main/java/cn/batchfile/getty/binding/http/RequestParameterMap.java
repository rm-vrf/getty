package cn.batchfile.getty.binding.http;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.batchfile.getty.exceptions.InvalidOperationException;

public class RequestParameterMap implements Map<String, Object> {

	private static final Logger logger = Logger.getLogger(RequestParameterMap.class);
	private HttpServletRequest servletRequest;
	
	public RequestParameterMap(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
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
		throw new InvalidOperationException("request parameter cannot be changed");
	}

	@Override
	public Object remove(Object name) {
		throw new InvalidOperationException("request parameter cannot be changed");
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> values) {
		throw new InvalidOperationException("request parameter cannot be changed");
	}

	@Override
	public void clear() {
		throw new InvalidOperationException("request parameter cannot be changed");
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
		if (logger.isDebugEnabled()) {
			logger.debug("content-type is: " + servletRequest.getContentType());
		}
		Map<String, Object> map = new HashMap<String, Object>();
	
		Enumeration<String> names = servletRequest.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = servletRequest.getParameter(name);
			map.put(name, value);
		}
		return map;
	}
}
