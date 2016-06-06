package cn.batchfile.getty.binding.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class Response {

	private org.eclipse.jetty.websocket.api.Session session;
	private String contentType;
	private String charset;
	private ResponseHeaderMap headers;
	
	public Response(org.eclipse.jetty.websocket.api.Session session) {
		this.session = session;
		headers = new ResponseHeaderMap(session.getUpgradeResponse().getHeaders());
	}
	
	public String getContentType() {
		String contentType = session.getUpgradeResponse().getHeader("Content-Type");
		contentType = StringUtils.substringBefore(contentType, ";");
		this.contentType = contentType;
		return this.contentType;
	}
	
	public Response setContentType(String contentType) {
		this.contentType = contentType;
		session.getUpgradeResponse().setHeader("Content-Type", String.format("%s; charset=%s", contentType, charset));
		return this;
	}
	
	public String getCharset() {
		String contentType = session.getUpgradeResponse().getHeader("Content-Type");
		String charset = StringUtils.substringAfter(contentType, "charset=");
		if (StringUtils.isEmpty(charset)) {
			this.charset = Charset.defaultCharset().name();
		} else {
			this.charset = charset;
		}
		return this.charset;
	}
	
	public Response setCharset(String charset) {
		this.charset = charset;
		session.getUpgradeResponse().setHeader("Content-Type", String.format("%s; charset=%s", contentType, charset));
		return this;
	}
	
	public ResponseHeaderMap getHeaders() {
		return headers;
	}
	
	public int getStatus() {
		return session.getUpgradeResponse().getStatusCode();
	}
	
	public Response setStatus(int status) {
		session.getUpgradeResponse().setStatusCode(status);
		return this;
	}
	
	public Response print(String message) throws IOException {
		session.getRemote().sendString(message);
		return this;
	}
	
	public Response println(String message) throws IOException {
		session.getRemote().sendString(message + IOUtils.LINE_SEPARATOR);
		return this;
	}
	
	public Response write(InputStream inputStream) throws IOException {
		byte[] bytes = IOUtils.toByteArray(inputStream);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		session.getRemote().sendBytes(buffer);
		return this;
	}
	
	public Response write(byte[] data) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(data); 
		session.getRemote().sendBytes(buffer);
		return this;
	}
	
	public Response write(Reader reader) throws IOException {
		byte[] bytes = IOUtils.toByteArray(reader);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		session.getRemote().sendBytes(buffer);
		return this;
	}
	
	public Response error(int code, String message) throws IOException {
		session.getUpgradeResponse().setStatusCode(code);
		session.getUpgradeResponse().setStatusReason(message);
		return this;
	}
	
	public Response error(int code) throws IOException {
		session.getUpgradeResponse().setStatusCode(code);
		return this;
	}	
}
