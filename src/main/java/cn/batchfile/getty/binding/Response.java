package cn.batchfile.getty.binding;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class Response {

	private HttpServletResponse servletResponse;
	private ResponseHeaderMap responseHeaderMap;
	
	public Response(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
		this.responseHeaderMap = new ResponseHeaderMap(this.servletResponse);
	}
	
	public String getContentType() {
		return servletResponse.getContentType();
	}
	
	public Response setContentType(String contentType) {
		servletResponse.setContentType(contentType);
		return this;
	}
	
	public String getCharset() {
		return servletResponse.getCharacterEncoding();
	}
	
	public Response setCharset(String charset) {
		servletResponse.setCharacterEncoding(charset);
		return this;
	}
	
	public ResponseHeaderMap getHeaders() {
		return responseHeaderMap;
	}
	
	public Response print(Object object) throws IOException {
		servletResponse.getWriter().print(object);
		return this;
	}
	
	public Response println(Object object) throws IOException {
		servletResponse.getWriter().println(object);
		return this;
	}
	
	public Response write(InputStream inputStream) throws IOException {
		IOUtils.copy(inputStream, servletResponse.getOutputStream());
		return this;
	}
	
	public Response write(byte[] data) throws IOException {
		IOUtils.write(data, servletResponse.getOutputStream());
		return this;
	}
	
	public Response write(Reader reader) throws IOException {
		IOUtils.copy(reader, servletResponse.getOutputStream());
		return this;
	}
	
	public Response error(int code, String message) throws IOException {
		servletResponse.sendError(code, message);
		return this;
	}

	public Response error(int code) throws IOException {
		servletResponse.sendError(code);
		return this;
	}
}
