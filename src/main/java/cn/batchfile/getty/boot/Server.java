package cn.batchfile.getty.boot;

import java.io.File;
import java.util.Date;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileMonitor;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.log4j.Logger;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import cn.batchfile.getty.binding.Application;
import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.mvc.RequestMapping;
import cn.batchfile.getty.mvc.Rewriter;
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
		
		//start jetty
		startJetty(configuration);
		logger.info(String.format("<Getty startup at port %d>",
				configuration.getPort()));
		
	}
	
	public void stop() {
		
	}
	
	private void startJetty(Configuration configuration) throws Exception {
		// create jetty server
		final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(
				configuration.getPort());
		setRuntimeParameters(server, configuration);
		
		//load rewriter mapper
		final String webDirectory = configuration.getWebRoot();
		final Rewriter rewriter = new Rewriter();
		rewriter.config(new File(webDirectory));
		
		//add file watcher on webapp
		ConfigFileListener listener = new ConfigFileListener();
		DefaultFileMonitor fm = new DefaultFileMonitor(listener);
		listener.setFileMonitor(fm);
		listener.setRewriter(rewriter);
		listener.setRoot(webDirectory);
		fm.setDelay(2000);
		fm.setRecursive(false);
		FileObject file = VFS.getManager().resolveFile(webDirectory);
		listener.addFile(fm, file);
		
		fm.start();
		logger.info("add watcher on directory: " + webDirectory);
		
		// setup webapp
		WebAppContext context = new WebAppContext();
		context.setContextPath(configuration.getContextPath());
		context.setWar(webDirectory);
		context.setWelcomeFiles(configuration.getIndexPages());
		context.setServer(server);
		
		HashLoginService loginService = new HashLoginService("TEST-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(loginService);
		server.setHandler(context);
		
		// setup servlet mapping
		RequestMapping mapping = new RequestMapping(configuration, rewriter);
		GettyServlet servlet = new GettyServlet(mapping);
		context.addServlet(new ServletHolder(servlet), "/");
		
		// kick off http service
		server.start();
		
		// set start time & config
		Application.getInstance().setStartTime(new Date());
		Application.getInstance().setConfiguration(configuration);
	}
	
	private void setRuntimeParameters(org.eclipse.jetty.server.Server server, Configuration configuration) {
		ThreadPool pool = server.getThreadPool();
		if (pool instanceof QueuedThreadPool) {
			QueuedThreadPool qtp = (QueuedThreadPool)pool;
			if (configuration.getMaxIdleTime() > 0) {
				qtp.setIdleTimeout(configuration.getMaxIdleTime());
			}
			if (configuration.getMaxThread() > 0) {
				qtp.setMaxThreads(configuration.getMaxThread());
			}
			if (configuration.getMinThread() > 0) {
				qtp.setMinThreads(configuration.getMinThread());
			}
			qtp.setName("getty-http");
		}
	}
	
	class ConfigFileListener implements FileListener {
		private FileMonitor fileMonitor;
		private Rewriter rewriter;
		private String root;
		
		public FileMonitor getFileMonitor() {
			return fileMonitor;
		}

		public void setFileMonitor(FileMonitor fileMonitor) {
			this.fileMonitor = fileMonitor;
		}

		public Rewriter getRewriter() {
			return rewriter;
		}

		public void setRewriter(Rewriter rewriter) {
			this.rewriter = rewriter;
		}

		public String getRoot() {
			return root;
		}

		public void setRoot(String root) {
			this.root = root;
		}

		@Override
		public void fileChanged(FileChangeEvent event) throws Exception {
			if (event.getFile().getType() == FileType.FOLDER) {
				addFile(fileMonitor, event.getFile());
			} else if (event.getFile().getName().getBaseName().equals(Rewriter.CONFIG_FILE)) {
				rewriter.config(new File(root));
			}
		}

		@Override
		public void fileCreated(FileChangeEvent event) throws Exception {
			if (event.getFile().getType() == FileType.FOLDER) {
				addFile(fileMonitor, event.getFile());
			} else if (event.getFile().getName().getBaseName().equals(Rewriter.CONFIG_FILE)) {
				rewriter.config(new File(root));
			}
		}

		@Override
		public void fileDeleted(FileChangeEvent event) throws Exception {
			if (event.getFile().getType() == FileType.FOLDER) {
				fileMonitor.removeFile(event.getFile());
			} else if (event.getFile().getName().getBaseName().equals(Rewriter.CONFIG_FILE)) {
				rewriter.config(new File(root));
			}
		}

		public void addFile(FileMonitor fm, FileObject file) throws FileSystemException {
			if (file.getType() == FileType.FOLDER) {
				fm.addFile(file);
				for (FileObject child : file.getChildren()) {
					addFile(fm, child);
				}
			} 
		}
	}
}
