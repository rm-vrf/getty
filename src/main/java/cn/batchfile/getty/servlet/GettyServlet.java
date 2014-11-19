package cn.batchfile.getty.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.exceptions.ListDirectoryNotAllowedException;
import cn.batchfile.getty.exceptions.RewriteMappingException;
import cn.batchfile.getty.mvc.RequestMapping;
import cn.batchfile.getty.parser.DirectoryParser;
import cn.batchfile.getty.parser.GroovyParser;
import cn.batchfile.getty.parser.Parser;
import cn.batchfile.getty.parser.StaticParser;

public class GettyServlet implements Servlet {

	private static final Logger logger = Logger.getLogger(GettyServlet.class);
	private RequestMapping requestMapping;
	private Map<String, Parser> parsers = new HashMap<String, Parser>();
	
	public GettyServlet(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}

	public GettyServlet requestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
		return this;
	}

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
		//init parsers
		parsers.put("static", new StaticParser(requestMapping.configuration()));
		parsers.put("dir", new DirectoryParser(requestMapping.configuration()));
		parsers.put("groovy", new GroovyParser(requestMapping.configuration()));
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		logger.debug("Dispatch servlet work");
		HttpServletRequest h_request = (HttpServletRequest)request;
		HttpServletResponse h_response = (HttpServletResponse)response; 
		File file = null;
		Map<String, Object> vars = new HashMap<String, Object>();
		try {
			file = requestMapping.mapping(h_request, vars);
		} catch (ListDirectoryNotAllowedException e) {
			h_response.sendError(401, "Directory list not allowed");
			return;
		} catch (RewriteMappingException e) {
			h_response.sendError(410, e.getMessage());
			return;
		}
		
		if (file == null || !file.exists()) {
			h_response.sendError(404);
			return;
		} else {
			dispatch(file, h_request, h_response, vars);
		}
	}
	
	private void dispatch(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) throws IOException {
		if (file.isDirectory()) {
			parsers.get("dir").parse(file, request, response, vars);
		} else if (StringUtils.endsWith(file.getName(), Parser.GROOVY_EXTENSION)) {
			parsers.get("groovy").parse(file, request, response, vars);
		} else {
			parsers.get("static").parse(file, request, response, vars);
		}
	}
}
