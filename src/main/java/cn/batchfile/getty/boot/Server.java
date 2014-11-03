package cn.batchfile.getty.boot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.filter.GettyFilter;
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
	 */
	public void start(Configuration configuration) {
		logger.info(String.format("<Getty startup at port %d>",
				configuration.port()));

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(configuration.port());
		if (configuration.maxIdleTime() > 0) {
			connector.setMaxIdleTime(configuration.maxIdleTime());
		}
		if (configuration.requestHeaderSize() > 0) {
			connector.setRequestHeaderSize(configuration.requestHeaderSize());
		}

		QueuedThreadPool threadPool = new QueuedThreadPool();
		if (configuration.maxIdleTime() > 0) {
			threadPool.setMaxIdleTimeMs(configuration.maxIdleTime());
		}
		if (configuration.maxQueued() > 0) {
			threadPool.setMaxQueued(configuration.maxQueued());
		}
		if (configuration.maxThread() > 0) {
			threadPool.setMaxThreads(configuration.maxThread());
		}
		if (configuration.minThread() > 0) {
			threadPool.setMinThreads(configuration.minThread());
		}
		threadPool.setName("getty-http");
		connector.setThreadPool(threadPool);

		final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(
				configuration.port());
		server.setConnectors(new Connector[] { connector });

		// Servlet ContextHandler context = new ServletContextHandler(server,
		// configuration.contextPath());
		URL warUrl = null;
		try {
			warUrl = new File(configuration.baseDirectory()
					+ configuration.webRoot()).toURI().toURL();
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			return;
		}
		final String warUrlString = warUrl.toExternalForm();
		ServletContextHandler context = new WebAppContext(warUrlString,
				configuration.contextPath());
		server.setHandler(context);

		// add filter
		context.addFilter(new FilterHolder(new GettyFilter()), "/",
				FilterMapping.DEFAULT);

		// add servlet
		RequestMapping mapping = new RequestMapping();
		mapping.configuration(configuration);
		GettyServlet servlet = new GettyServlet();
		servlet.requestMapping(mapping);
		context.addServlet(new ServletHolder(servlet), "*.groovy");

		try {
			new Thread(new Runnable() {
				public void run() {
					try {
						server.start();
						server.join();
					} catch (Exception e) {
						logger.error("Error when start Getty", e);
					}
				}
			}).start();
		} catch (Exception e) {
			logger.error("Error when start Getty", e);
		}
	}
}
