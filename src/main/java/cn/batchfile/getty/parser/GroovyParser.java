package cn.batchfile.getty.parser;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.binding.Cookie;
import cn.batchfile.getty.binding.Model;
import cn.batchfile.getty.binding.Request;
import cn.batchfile.getty.binding.Response;
import cn.batchfile.getty.binding.Session;
import cn.batchfile.getty.binding.View;
import cn.batchfile.getty.configuration.Configuration;

public class GroovyParser extends Parser {
	
	private static final Logger logger = Logger.getLogger(GroovyParser.class);
	private Configuration configuration;
	
	public GroovyParser(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void parse(File file, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		//binding object
		Request _request = new Request(request);
		Response _response = new Response(response);
		Session _session = new Session(request);
		Cookie _cookie = new Cookie(request, response);
		Model _model = new Model(request);
		View _view = new View(request, response);
		Logger _logger = Logger.getLogger(file.getName());
		
		Binding binding = new Binding();
		binding.setProperty("_request", _request);
		binding.setProperty("_response", _response);
		binding.setProperty("_session", _session);
		binding.setProperty("_cookie", _cookie);
		binding.setProperty("_model", _model);
		binding.setProperty("_view", _view);
		binding.setProperty("_logger", _logger);
		
		//binding input param
		for (Entry<String, Object> entry : _request.parameters().entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}
		
		//set response charset
		response.setCharacterEncoding(configuration.charset());

		//execute script file
		GroovyShell shell = new GroovyShell(binding);
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			List<String> lines = IOUtils.readLines(stream, configuration.fileEncoding());
			String scriptText = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
			String base = configuration.baseDirectory() + File.separatorChar + configuration.webRoot();
			Object r = shell.evaluate(scriptText, file.getName(), base);
			if (logger.isDebugEnabled()) {
				logger.debug("shell return value: " + r);
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
		
		//process content-type
		if (StringUtils.isEmpty(response.getContentType())) {
			response.setContentType("text/html");
		}
	}
}
