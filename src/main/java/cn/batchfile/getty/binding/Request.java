package cn.batchfile.getty.binding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Request {
	private static final Logger logger = Logger.getLogger(Request.class);
	private HttpServletRequest servletRequest;
	private String body;
	private boolean bodyInited;
	
	public Request(HttpServletRequest servletRequest) throws IOException {
		this.servletRequest = servletRequest;
	}
	
	public String uri() {
		return servletRequest.getRequestURI();
	}
	
	public String contentType() {
		return servletRequest.getContentType();
	}
	
	public String charset() {
		return servletRequest.getCharacterEncoding();
	}
	
	public String header(String name) {
		return servletRequest.getHeader(name);
	}
	
	public Map<String, String> headers() {
		Map<String, String> headers = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Enumeration<String> names = servletRequest.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = servletRequest.getHeader(name);
			headers.put(name, value);
		}
		return headers;
	}
	
	public Object parameter(String name) {
		String[] value = servletRequest.getParameterValues(name);
		if (value != null && value.length == 1) {
			return value[0];
		} else {
			return value;
		}
	}
	
	public Map<String, Object> parameters() {
		if (logger.isDebugEnabled()) {
			logger.debug("content-type is: " + servletRequest.getContentType());
		}
		Map<String, Object> params = new HashMap<String, Object>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> names = servletRequest.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object value = parameter(name);
			params.put(name, value);
		}
		return params;
	}
	
	public String body() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("content-type is: " + servletRequest.getContentType());
		}
		if (!bodyInited) {
			InputStream stream = null;
			try {
				stream = servletRequest.getInputStream();
				List<String> lines = IOUtils.readLines(stream, charset());
				body = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
			} finally {
				IOUtils.closeQuietly(stream);
			}
			bodyInited = true;
		}
		return body;
	}
}
