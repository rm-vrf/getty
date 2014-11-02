package cn.batchfile.getty.binding;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class Request {
	private static final String LINE_SEP = System.getProperty("line.separator", "\r\n");
	private HttpServletRequest servletRequest;
	
	public Request(HttpServletRequest servletRequest) throws IOException {
		this.servletRequest = servletRequest;
	}
	
	public String getUri() {
		return servletRequest.getRequestURI();
	}
	
	public String getBody() throws IOException {
		InputStream stream = null;
		try {
			stream = servletRequest.getInputStream();
			List<String> lines = IOUtils.readLines(stream);
			return StringUtils.join(lines, LINE_SEP);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
