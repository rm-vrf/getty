package cn.batchfile.getty.binding;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class Response {

	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private ResponseHeaderMap responseHeaderMap;
	
	public Response(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		this.servletRequest = servletRequest;
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
	
	public int getStatus() {
		return servletResponse.getStatus();
	}
	
	public Response setStatus(int status) {
		servletResponse.setStatus(status);
		return this;
	}
	
	public String getLocale() {
		return servletResponse.getLocale().toString();
	}
	
	public Response setLocale(String locale) {
		servletResponse.setLocale(new Locale(locale));
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
	
	public Response redirect(String location) throws IOException {
		servletResponse.sendRedirect(location);
		return this;
	}
	
	public Response json(Object object) throws IOException {
		servletResponse.setContentType("application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(servletResponse.getWriter(), object);
		return this;
	}

	public Response jsp(String view) throws ServletException, IOException {
		RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(view);
		dispatcher.forward(servletRequest, servletResponse);
		return this;
	}
}
