package cn.batchfile.getty.binding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Response {

	private HttpServletResponse servletResponse;
	
	public Response(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public String contentType() {
		return servletResponse.getContentType();
	}
	
	public Response contentType(String contentType) {
		servletResponse.setContentType(contentType);
		return this;
	}
	
	public String charset() {
		return servletResponse.getCharacterEncoding();
	}
	
	public Response charset(String charset) {
		servletResponse.setCharacterEncoding(charset);
		return this;
	}
	
	public Response header(String name, String value) {
		servletResponse.addHeader(name, value);
		return this;
	}
	
	public Response print(Object object) throws IOException {
		servletResponse.getWriter().print(object);
		return this;
	}
	
	public Response println(Object object) throws IOException {
		servletResponse.getWriter().println(object);
		return this;
	}
	
	public void error(int code, String message) throws IOException {
		servletResponse.sendError(code, message);
	}

	public void error(int code) throws IOException {
		servletResponse.sendError(code);
	}
}
