package cn.batchfile.getty.binding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
	
	public Request(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	
	public String method() {
		return servletRequest.getMethod();
	}
	
	public String queryString() {
		return servletRequest.getQueryString();
	}
	
	public int contentLength() {
		return servletRequest.getContentLength();
	}
	
	public String locale() {
		return servletRequest.getLocale().toString();
	}
	
	public List<String> locales() {
		List<String> list = new ArrayList<String>();
		Enumeration<Locale> enums = servletRequest.getLocales();
		while (enums.hasMoreElements()) {
			list.add(enums.nextElement().toString());
		}
		return list;
	}
	
	public String localAddress() {
		return servletRequest.getLocalAddr();
	}
	
	public String localName() {
		return servletRequest.getLocalName();
	}
	
	public int localPort() {
		return servletRequest.getLocalPort();
	}
	
	public String protocol() {
		return servletRequest.getProtocol();
	}
	
	public String remoteUser() {
		return servletRequest.getRemoteUser();
	}
	
	public String remoteAddress() {
		return servletRequest.getRemoteAddr();
	}
	
	public String remoteHost() {
		return servletRequest.getRemoteHost();
	}
	
	public String serverName() {
		return servletRequest.getServerName();
	}
	
	public int serverPort() {
		return servletRequest.getServerPort();
	}
	
	public String schema() {
		return servletRequest.getScheme();
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
