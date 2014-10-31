package cn.batchfile.getty.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class GettyServlet implements Servlet {
	
	private static final Logger logger = Logger.getLogger(GettyServlet.class);

	@Override
	public void destroy() {
		// pass

	}

	@Override
	public ServletConfig getServletConfig() {
		// pass
		return null;
	}

	@Override
	public String getServletInfo() {
		// pass
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// pass

	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		
		logger.debug("Dispatch servlet work");
	}

}
