package cn.batchfile.getty.filter;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.binding.Application;
import cn.batchfile.getty.binding.Cookie;
import cn.batchfile.getty.binding.Request;
import cn.batchfile.getty.binding.Response;
import cn.batchfile.getty.binding.Session;
import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.exceptions.ScriptException;

public class FilterListener implements Filter{
	public static final String CONFIG_FILE = "_filter.groovy";
	private static final Logger LOG = Logger.getLogger(FilterListener.class);
	private Map<String, File> files = new HashMap<String, File>();
	private static final Logger logger = Logger.getLogger(CONFIG_FILE);
	private Configuration configuration;
	private Application application;

	public FilterListener(Configuration configuration, Application application) {
		this.configuration = configuration;
		this.application = application;
	}

	public void config(File directory) {
		files.clear();
		configDir(directory);
	}

	private void configDir(File directory) {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				configDir(file);
			} else if (file.getName().equals(CONFIG_FILE)) {
				LOG.info(String.format("Load filter: %s", file.getPath()));
				String path = file.getParentFile().getPath();
				path = StringUtils.substring(path, configuration.getWebRoot().length());
				path = StringUtils.replace(path, "\\", "/");
				this.files.put(path, file);
			}
		}
	}
	
	@Override
	public void destroy() {
		//pass
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		boolean run = runScripts(files, (HttpServletRequest)request, (HttpServletResponse)response, chain);
		
		if (!run) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		//pass
	}
	
	private boolean runScripts(Map<String, File> files, 
			HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String uri = request.getRequestURI();
		boolean run = false;
		
		for (Entry<String, File> entry : files.entrySet()) {
			String path = entry.getKey();
			if (StringUtils.startsWith(uri + "/", path + "/")) {
				run = true;
				Object r = runScript(entry.getValue(), "before", request, response);
				if (r == null || Boolean.parseBoolean(r.toString()) != false) {
					chain.doFilter(request, response);
				}
				runScript(entry.getValue(), "after", request, response);
			}
		}
		return run;
	}

	private Object runScript(File file, String method, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Binding binding = new Binding();
		binding.setProperty("$logger", logger);
		binding.setProperty("$application", application);
		Request _request = new Request(request);
		Response _response = new Response(response);
		Session _session = new Session(request);
		Cookie _cookie = new Cookie(request, response);
		binding.setProperty("$request", _request);
		binding.setProperty("$response", _response);
		binding.setProperty("$session", _session);
		binding.setProperty("$cookie", _cookie);

		GroovyShell shell = new GroovyShell(binding);
		InputStream stream = null;
		Object r = null;
		try {
			stream = new FileInputStream(file);
			List<String> lines = IOUtils.readLines(stream, configuration.getFileEncoding());
			String scriptText = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
			Script s = shell.parse(scriptText, file.getName());
			try {
				r = s.invokeMethod(method, null);
				if (LOG.isDebugEnabled()) {
					LOG.debug("shell return value: " + r);
				}
			} catch (Exception e) {
				LOG.error(String.format("error when run script: %s", file.getAbsolutePath()), e);
				throw new ScriptException(e);
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return r;
	}
}
