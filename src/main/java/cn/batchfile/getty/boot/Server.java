package cn.batchfile.getty.boot;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.mvc.RequestMapping;
import cn.batchfile.getty.servlet.GettyServlet;

/**
 * Getty服务
 * 
 * @author htlu
 *
 */
public class Server {
	private static final Logger logger = Logger.getLogger(Server.class);

	/**
	 * 启动服务
	 * 
	 * @param configuration
	 *            设置
	 * @throws Exception 
	 */
	public void start(Configuration configuration) throws Exception {
		logger.info(String.format("<Getty startup at port %d>",
				configuration.port()));
		
		// create jetty server
		final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(
				configuration.port());
		setRuntimeParameters(server, configuration);
		
		// setup webapp
		WebAppContext context = new WebAppContext();
		context.setContextPath(configuration.contextPath());
		String war = configuration.baseDirectory() + File.separatorChar + configuration.webRoot();
		context.setWar(war);
		context.setWelcomeFiles(configuration.indexPages());
		context.setServer(server);
		
		HashLoginService loginService = new HashLoginService("TEST-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(loginService);
		server.setHandler(context);
		
		// setup servlet mapping
		RequestMapping mapping = new RequestMapping(configuration);
		GettyServlet servlet = new GettyServlet(mapping);
		context.addServlet(new ServletHolder(servlet), "/");
		
		// kick off
		server.start();
		
//		URL warUrl = null;
//		try {
//			warUrl = new File(configuration.baseDirectory() + File.separatorChar
//					+ configuration.webRoot()).toURI().toURL();
//		} catch (MalformedURLException e) {
//			logger.error(e.getMessage(), e);
//			return;
//		}
//		final String warUrlString = warUrl.toExternalForm();
//		ServletContextHandler context = new WebAppContext(warUrlString,
//				configuration.contextPath());
//		server.setHandler(context);
//
//		// add filter
//		context.addFilter(new FilterHolder(new GettyFilter()), "/",
//				FilterMapping.DEFAULT);
//
//		// add servlet
//		RequestMapping mapping = new RequestMapping();
//		mapping.configuration(configuration);
//		GettyServlet servlet = new GettyServlet();
//		servlet.requestMapping(mapping);
//		context.addServlet(new ServletHolder(servlet), "*.groovy");
//		context.setWelcomeFiles(configuration.indexPages());
//
//		try {
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						server.start();
//						server.join();
//					} catch (Exception e) {
//						logger.error("Error when start Getty", e);
//					}
//				}
//			}).start();
//		} catch (Exception e) {
//			logger.error("Error when start Getty", e);
//		}
	}
	
	private void setRuntimeParameters(org.eclipse.jetty.server.Server server, Configuration configuration) {
		ThreadPool pool = server.getThreadPool();
		if (pool instanceof QueuedThreadPool) {
			QueuedThreadPool qtp = (QueuedThreadPool)pool;
			if (configuration.maxIdleTime() > 0) {
				qtp.setIdleTimeout(configuration.maxIdleTime());
			}
			if (configuration.maxThread() > 0) {
				qtp.setMaxThreads(configuration.maxThread());
			}
			if (configuration.minThread() > 0) {
				qtp.setMinThreads(configuration.minThread());
			}
			qtp.setName("getty-http");
		}
	}
}
