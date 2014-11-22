package cn.batchfile.getty.servlet;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.binding.Application;
import cn.batchfile.getty.configuration.Configuration;

public class SessionListener implements HttpSessionListener {

	public static final String CONFIG_FILE = "_session.groovy";
	private static final Logger LOG = Logger.getLogger(SessionListener.class);
	private List<File> files = new ArrayList<File>();
	private static final Logger logger = Logger.getLogger(CONFIG_FILE);
	private Configuration configuration;
	private Application application;
	
	public SessionListener(Configuration configuration, Application application) {
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
				LOG.info(String.format("Load application listener: %s", file.getPath()));
				this.files.add(file);
			}
		}
	}

	private void create(HttpSession session) throws IOException {
		LOG.debug("run session create script");
		runScripts(files, "create", session);
	}
	
	private void destroy(HttpSession session) throws IOException {
		LOG.debug("run session destroy script");
		runScripts(files, "destroy", session);
	}

	private void runScripts(List<File> files, String method, Object session) throws IOException {
		for (File file : files) {
			LOG.info("run script: " + file.getAbsolutePath());
			runScript(file, method, session);
		}
	}

	private void runScript(File file, String method, Object session) throws IOException {
		Binding binding = new Binding();
		binding.setProperty("$session", session);
		binding.setProperty("$application", application);
		binding.setProperty("$logger", logger);

		GroovyShell shell = new GroovyShell(binding);
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			List<String> lines = IOUtils.readLines(stream, configuration.getFileEncoding());
			String scriptText = StringUtils.join(lines, IOUtils.LINE_SEPARATOR);
			Script s = shell.parse(scriptText, file.getName());
			try {
				Object r = s.invokeMethod(method, null);
				if (LOG.isDebugEnabled()) {
					LOG.debug("shell return value: " + r);
				}
			} catch (Exception e) {
				LOG.error(String.format("error when run script: %s", file.getAbsolutePath()), e);
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		try {
			create(se.getSession());
		} catch (IOException e) {
			LOG.error("error when create session", e);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			destroy(se.getSession());
		} catch (IOException e) {
			LOG.error("error when destroy session", e);
		}
	}
}
