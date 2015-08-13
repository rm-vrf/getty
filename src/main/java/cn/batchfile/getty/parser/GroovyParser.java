package cn.batchfile.getty.parser;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.batchfile.getty.binding.Application;
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
	private CommonsMultipartResolver multipartResolver;
	
	public GroovyParser(Configuration configuration) {
		this.configuration = configuration;
		multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(-1);
		multipartResolver.setDefaultEncoding("UTF-8");
	}

	@Override
	public void parse(File file, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> vars) throws IOException {
		
		//binding object
		Application _application = Application.getInstance();
		Request _request = new Request(request, multipartResolver);
		Response _response = new Response(response);
		Session _session = new Session(request);
		Cookie _cookie = new Cookie(request, response);
		Model _model = new Model(request);
		View _view = new View(request, response);
		Logger _logger = Logger.getLogger(file.getName());
		
		Binding binding = new Binding();
		binding.setProperty("$application", _application);
		binding.setProperty("$request", _request);
		binding.setProperty("$response", _response);
		binding.setProperty("$session", _session);
		binding.setProperty("$cookie", _cookie);
		binding.setProperty("$model", _model);
		binding.setProperty("$view", _view);
		binding.setProperty("$logger", _logger);
		
		//binging path vars
		for (Entry<String, Object> entry : vars.entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}
		
		//binding input param
		for (Entry<String, Object> entry : _request.getParameters().entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}
		
		//set response charset
		response.setCharacterEncoding(configuration.getCharset());

		//execute script file
		GroovyShell shell = new GroovyShell(binding);
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			List<String> lines = IOUtils.readLines(stream, configuration.getFileEncoding());
			String scriptText = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
			String base = configuration.getWebRoot();
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
