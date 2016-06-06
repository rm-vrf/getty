package cn.batchfile.getty.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import groovy.util.GroovyScriptEngine;

public class WebSocketManager extends WebSocketServlet {

	private static final Logger LOG = Logger.getLogger(WebSocketManager.class);
	private static final long serialVersionUID = 6471275025352138121L;
	private Map<String, GroovyScriptEngine> gses = new ConcurrentHashMap<String, GroovyScriptEngine>();
	private Application application;
	private ApplicationInstance applicationInstance;
	private ClassLoader classLoader;

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public ApplicationInstance getApplicationInstance() {
		return applicationInstance;
	}

	public void setApplicationInstance(ApplicationInstance applicationInstance) {
		this.applicationInstance = applicationInstance;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new WebSocketCreator() {
			@Override
			public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
				LOG.debug("create websocket servlet");
				WebSocketInstance wsi = new WebSocketInstance();
				wsi.setGses(gses);
				wsi.setApplication(application);
				wsi.setApplicationInstance(applicationInstance);
				wsi.setClassLoader(classLoader);
				return wsi;
			}
		});
	}
}
