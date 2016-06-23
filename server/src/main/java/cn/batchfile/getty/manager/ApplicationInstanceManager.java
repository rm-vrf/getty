package cn.batchfile.getty.manager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.lang.ApplicationClassLoader;

public class ApplicationInstanceManager {
	
	private static final Logger LOG = Logger.getLogger(ApplicationInstanceManager.class);
	private Map<String, ApplicationEventListener> applicationEventListeners = new HashMap<String, ApplicationEventListener>();

	public ApplicationInstance start(Application application) throws MalformedURLException {
		ApplicationInstance ai = new ApplicationInstance();
		ai.setApplication(application);
		ai.setStartTime(new Date());
		
		Server server = new Server(application.getPort());
		setRuntimeParameters(server);
		
		WebAppContext context = new WebAppContext();
		context.setMaxFormContentSize(Integer.MAX_VALUE);
		context.setContextPath("/");
		context.setWar(application.getDir().getAbsolutePath());
		context.setServer(server);
		
		HashLoginService loginService = new HashLoginService("GETTY-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(loginService);
		server.setHandler(context);
		
		//create classloader
		ClassLoader cl = createClassLoader(application);
		
		//create script manager
		ScriptEngineManager sem = createScriptEngineManager(application, cl);

		//create servlet
		ServletManager servletManager = createServletManager(application, ai, cl, sem);
		
		//apply servlet
		context.addServlet(new ServletHolder(servletManager), "/");
		
		//apply websocket
		if (application.getWebSocket() != null) {
			WebSocketManager socketManager = createWebSocketManager(application, ai, cl, sem);
			context.addServlet(new ServletHolder(socketManager), application.getWebSocket().getUrlPattern());
		}

		//set session listener
		SessionEventListener sessionEventListener = createSessionEventListener(application, ai, sem);
		context.addEventListener(sessionEventListener);
		
		//set session timeout
		int minutes = application.getSession() == null ? 0 : application.getSession().getTimeout();
		if (minutes > 0) {
			context.getSessionHandler().getSessionManager().setMaxInactiveInterval(minutes * 60);
		}
		
		//create applicaiton listener
		ApplicationEventListener ael = createApplicationEventListener(server, application, ai, sem);
		applicationEventListeners.put(application.getName(), ael);
		
		try {
			//启动web服务
			server.start();
			int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
			
			LOG.info(String.format("start %s at port: %s", application.getName(), port));
			ai.setPort(port);
			
			//执行应用监听器
			ael.applicationStart();
		} catch (Exception e) {
			throw new RuntimeException("error when start web server", e);
		}
		
		return ai;
	}
	
	public void stop(String name) {
		ApplicationEventListener listener = applicationEventListeners.get(name);
		if (listener != null) {
			
			//stop web server
			try {
				listener.getServer().stop();
			} catch (Exception e) {
			}
			
			//execute application listener
			try {
				listener.applicationStop();
			} catch (Exception e) {
			}
		}
	}

	private ApplicationEventListener createApplicationEventListener(Server server, Application application, ApplicationInstance applicationInstance, ScriptEngineManager scriptEngineManager) {
		ApplicationEventListener ael = new ApplicationEventListener();
		ael.setServer(server);
		ael.setApplication(application);
		ael.setApplicationInstance(applicationInstance);
		ael.setScriptEngineManager(scriptEngineManager);
		return ael;
	}
	
	private SessionEventListener createSessionEventListener(Application application, ApplicationInstance applicationInstance, ScriptEngineManager scriptEngineManager) {
		SessionEventListener listener = new SessionEventListener();
		listener.setApplication(application);
		listener.setApplicationInstance(applicationInstance);
		listener.setScriptEngineManager(scriptEngineManager);
		return listener;
	}

	private ScriptEngineManager createScriptEngineManager(Application application, ClassLoader classLoader) {
		ScriptEngineManager sem = new ScriptEngineManager();
		sem.setApplication(application);
		sem.setClassLoader(classLoader);
		return sem;
	}

	private ClassLoader createClassLoader(Application application) throws MalformedURLException {
		//构建classpath
		List<File> classpath = new ArrayList<File>();
		if (application.getClasses() != null && application.getClasses().exists()) {
			classpath.add(application.getClasses());
		}
		for (File lib : application.getLibs()) {
			classpath.add(lib);
		}
		
		URL[] urls = new URL[classpath.size()];
		for (int i = 0; i < classpath.size(); i ++) {
			urls[i] = classpath.get(i).toURI().toURL();
		}
		
		//构建classloader
		ApplicationClassLoader cl = new ApplicationClassLoader(urls, getClass().getClassLoader());
		return cl;
	}
	
	private WebSocketManager createWebSocketManager(Application application, ApplicationInstance instance, ClassLoader classLoader, ScriptEngineManager scriptEngineManager) {
		WebSocketManager wsm = new WebSocketManager();
		wsm.setApplication(application);
		wsm.setApplicationInstance(instance);
		wsm.setClassLoader(classLoader);
		wsm.setScriptEngineManager(scriptEngineManager);
		return wsm;
	}

	private ServletManager createServletManager(Application application, ApplicationInstance instance, ClassLoader classLoader, ScriptEngineManager scriptEngineManager) {
		MappingManager mapping = new MappingManager();
		mapping.setApplication(application);
		
		ServletManager servlet = new ServletManager();
		servlet.setApplication(application);
		servlet.setApplicationInstance(instance);
		servlet.setMappingManager(mapping);
		servlet.setClassLoader(classLoader);
		servlet.setScriptEngineManager(scriptEngineManager);
		
		return servlet;
	}

	private void setRuntimeParameters(Server server) {
		String s = System.getProperty("max.threads", "-1");
		int maxThreads = Integer.valueOf(s);
		s = System.getProperty("min.threads", "-1");
		int minThreads = Integer.valueOf(s);
		s = System.getProperty("max.idle.time", "-1");
		int maxIdleTime = Integer.valueOf(s);
		
		ThreadPool pool = server.getThreadPool();
		if (pool instanceof QueuedThreadPool) {
			QueuedThreadPool qtp = (QueuedThreadPool)pool;
			if (maxThreads > 0) {
				qtp.setMaxThreads(maxIdleTime);
			}
			if (minThreads > 0) {
				qtp.setMinThreads(minThreads);
			}
			if (maxIdleTime > 0) {
				qtp.setIdleTimeout(maxIdleTime);
			}
		}
	}
	
}
