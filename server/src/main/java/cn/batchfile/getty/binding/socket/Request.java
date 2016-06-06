package cn.batchfile.getty.binding.socket;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;

public class Request {

	private org.eclipse.jetty.websocket.api.Session session;
	private String text;
	private RequestHeaderMap headers;
	private RequestParameterMap parameters;
	
	public Request(org.eclipse.jetty.websocket.api.Session session, String text) {
		this.session = session;
		this.text = text;
		headers = new RequestHeaderMap(session.getUpgradeRequest().getHeaders());
		parameters = new RequestParameterMap(session.getUpgradeRequest().getParameterMap());
	}
	
	public String getMethod() {
		return session.getUpgradeRequest().getMethod();
	}
	
	public String getQueryString() {
		return session.getUpgradeRequest().getQueryString();
	}
	
	public int getContentLength() {
		return text.getBytes().length;
	}
	
	public String getLocalAddress() {
		return session.getLocalAddress().getAddress().getHostAddress();
	}
	
	public String getLocalName() {
		return session.getLocalAddress().getHostName();
	}
	
	public int getLocalPort() {
		return session.getLocalAddress().getPort();
	}
	
	public String getProtocol() {
		return session.getUpgradeRequest().getRequestURI().getScheme();
	}
	
	public String getRemoteUser() {
		return session.getUpgradeRequest().getRequestURI().getUserInfo();
	}
	
	public String getRemoteAddress() {
		return session.getRemoteAddress().getAddress().getHostAddress();
	}
	
	public String getRemoteHost() {
		return session.getRemoteAddress().getHostName();
	}
	
	public String getServerName() {
		return session.getLocalAddress().getHostName();
	}
	
	public int getServerPort() {
		return session.getLocalAddress().getPort();
	}
	
	public String getSchema() {
		return session.getUpgradeRequest().getRequestURI().getScheme();
	}
	
	public String getUri() {
		return session.getUpgradeRequest().getRequestURI().toString();
	}
	
	public String getContentType() {
		String contentType = session.getUpgradeRequest().getHeader("Content-Type");
		contentType = StringUtils.substringBefore(contentType, ";");
		return contentType;
	}
	
	public String getCharset() {
		String contentType = session.getUpgradeRequest().getHeader("Content-Type");
		String charset = StringUtils.substringAfter(contentType, "charset=");
		if (StringUtils.isEmpty(charset)) {
			return Charset.defaultCharset().name();
		} else {
			return charset;
		}
	}
	
	public RequestHeaderMap getHeaders() {
		return headers;
	}
	
	public RequestParameterMap getParameters() {
		return parameters;
	}
	
	public Object getBody() {
		return text;
	}
}
