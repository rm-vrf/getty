package cn.batchfile.getty.manager;

import java.util.Date;
import java.util.HashMap;
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

public class ApplicationInstanceManager {
	
	private static final Logger logger = Logger.getLogger(ApplicationInstanceManager.class);
	private Map<String, Server> servers = new HashMap<String, Server>();

	public ApplicationInstance start(Application application) {
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
		
		//create servlet
		ServletManager servletManager = createServletManager(application);

		//apply servlet
		context.addServlet(new ServletHolder(servletManager), "/");
		try {
			server.start();
			int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
			
			logger.info(String.format("start application %s at port: %s", application.getName(), port));
			ai.setPort(port);
		} catch (Exception e) {
			throw new RuntimeException("error when start web server", e);
		}
		
		return ai;
	}
	
	private ServletManager createServletManager(Application application) {
		MappingManager mapping = new MappingManager();
		mapping.setApplication(application);
		
		ServletManager servlet = new ServletManager();
		servlet.setApplication(application);
		servlet.setMappingManager(mapping);
		
		return servlet;
	}

	public void stop(String name) {
		try {
			servers.get(name).stop();
		} catch (Exception e) {
			//pass
		} finally {
			servers.remove(name);
		}
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
