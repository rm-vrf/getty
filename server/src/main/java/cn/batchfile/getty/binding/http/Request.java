package cn.batchfile.getty.binding.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class Request {
	private static final Logger logger = Logger.getLogger(Request.class);
	private Map<String, MultipartFile> files;
	private HttpServletRequest servletRequest;
	private RequestAttributeMap requestAttributeMap;
	private RequestHeaderMap requestHeaderMap;
	private RequestParameterMap requestParameterMap;
	private Object body;
	private boolean bodyInited;
	
	public Request(HttpServletRequest servletRequest) {
		this(servletRequest, null);
	}
	
	public Request(HttpServletRequest servletRequest, CommonsMultipartResolver multipartResolver) {
		if (multipartResolver != null && multipartResolver.isMultipart(servletRequest)) {
			MultipartHttpServletRequest mpr = multipartResolver.resolveMultipart(servletRequest);
			files = mpr.getFileMap();
			this.servletRequest = mpr;
		} else {
			this.servletRequest = servletRequest;
		}
		
		requestAttributeMap = new RequestAttributeMap(servletRequest);
		requestHeaderMap = new RequestHeaderMap(this.servletRequest);
		requestParameterMap = new RequestParameterMap(this.servletRequest);
	}
	
	public String getMethod() {
		return servletRequest.getMethod();
	}
	
	public String getQueryString() {
		return servletRequest.getQueryString();
	}
	
	public int getContentLength() {
		return servletRequest.getContentLength();
	}
	
	public String getLocale() {
		return servletRequest.getLocale().toString();
	}
	
	public List<String> getLocales() {
		List<String> list = new ArrayList<String>();
		Enumeration<Locale> enums = servletRequest.getLocales();
		while (enums.hasMoreElements()) {
			list.add(enums.nextElement().toString());
		}
		return list;
	}
	
	public String getLocalAddress() {
		return servletRequest.getLocalAddr();
	}
	
	public String getLocalName() {
		return servletRequest.getLocalName();
	}
	
	public int getLocalPort() {
		return servletRequest.getLocalPort();
	}
	
	public String getProtocol() {
		return servletRequest.getProtocol();
	}
	
	public String getRemoteUser() {
		return servletRequest.getRemoteUser();
	}
	
	public String getRemoteAddress() {
		return servletRequest.getRemoteAddr();
	}
	
	public String getRemoteHost() {
		return servletRequest.getRemoteHost();
	}
	
	public String getServerName() {
		return servletRequest.getServerName();
	}
	
	public int getServerPort() {
		return servletRequest.getServerPort();
	}
	
	public String getSchema() {
		return servletRequest.getScheme();
	}
	
	public String getUri() {
		return servletRequest.getRequestURI();
	}
	
	public String getContentType() {
		return servletRequest.getContentType();
	}
	
	public String getCharset() {
		return servletRequest.getCharacterEncoding();
	}
	
	public RequestAttributeMap getAttributes() {
		return requestAttributeMap;
	}

	public RequestHeaderMap getHeaders() {
		return requestHeaderMap;
	}
	
	public RequestParameterMap getParameters() {
		return requestParameterMap;
	}
	
	public List<MultipartFile> getFiles() {
		List<MultipartFile> list = new ArrayList<MultipartFile>();
		for (MultipartFile file : files.values()) {
			list.add(file);
		}
		return list;
	}
	
	public Object getBody() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("content-type is: " + servletRequest.getContentType());
		}
		if (!bodyInited) {
			InputStream stream = null;
			try {
				stream = servletRequest.getInputStream();
				List<String> lines = IOUtils.readLines(stream, getCharset());
				String s = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
				body = deserialize(s, servletRequest.getContentType());
			} finally {
				IOUtils.closeQuietly(stream);
			}
			bodyInited = true;
		}
		return body;
	}

	private Object deserialize(String s, String contentType) throws JsonParseException, JsonMappingException, IOException {
		Object r = null;
		if (StringUtils.equalsIgnoreCase("application/json", contentType)) {
			ObjectMapper mapper = new ObjectMapper();
			r = mapper.readValue(s, Object.class);
		} else {
			r = s;
		}
		return r;
	}
}
