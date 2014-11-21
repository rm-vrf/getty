package cn.batchfile.getty.binding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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
	
	public Response error(int code, String message) throws IOException {
		servletResponse.sendError(code, message);
		return this;
	}

	public Response error(int code) throws IOException {
		servletResponse.sendError(code);
		return this;
	}
}
