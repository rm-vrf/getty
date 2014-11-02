package cn.batchfile.getty.binding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Response {

	private HttpServletResponse servletResponse;
	
	public Response(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void print(Object object) throws IOException {
		servletResponse.getWriter().print(object);
	}
	
	public void println(Object object) throws IOException {
		servletResponse.getWriter().println(object);
	}
}
