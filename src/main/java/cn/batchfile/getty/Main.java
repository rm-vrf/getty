package cn.batchfile.getty;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.binding.Application;
import cn.batchfile.getty.boot.Server;
import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.util.Log4jConfigurator;

/**
 * Created by lane.cn on 14-10-27.
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		//set base directory
		File file = new File(".");
		String path = file.getAbsolutePath();
		if (StringUtils.endsWith(path, File.separator + ".")) {
			path = StringUtils.substring(path, 0, path.length() - (File.separator + ".").length());
		}
		Application.getInstance().setBaseDirectory(path);
		
		Configuration configuration = new Main().getConfiguration(path, args);
		
		new Log4jConfigurator().load(path, configuration);
		
		String n = IOUtils.LINE_SEPARATOR;
		String log = n;
		log += "  ____      _   _" + n;
		log += " / ___| ___| |_| |_ _   _" + n;
		log += "| |  _ / _ \\ __| __| | | |" + n;
		log += "| |_| |  __/ |_| |_| |_| |" + n;
		log += " \\____|\\___|\\__|\\__|\\__, |" + n;
		log += "                    |___/" + n;
		log += "Groovy on Jetty!" + n;
		logger.info(log);

		final Server server = new Server();
		server.start(configuration);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info("run into stop hook");
				server.stop();
			}
		});
	}
	
	private Configuration getConfiguration(String baseDirectory, String[] args) {
		Configuration configuration = new Configuration();
		
		//set args
		for (String arg : args) {
			if (getValue(arg, "port") != null) {
				configuration.setPort(Integer.valueOf(getValue(arg, "port")));
			} else if (getValue(arg, "max.thread") != null) {
				configuration.setMaxThread(Integer.valueOf(getValue(arg, "max.thread")));
			} else if (getValue(arg, "min.thread") != null) {
				configuration.setMinThread(Integer.valueOf(getValue(arg, "min.thread")));
			} else if (getValue(arg, "max.idle") != null) {
				configuration.setMaxIdleTime(Integer.valueOf(getValue(arg, "max.idle")));
			} else if (getValue(arg, "log.level") != null) {
				configuration.setLogLevel(getValue(arg, "log.level"));
			} else if (getValue(arg, "max.queued") != null) {
				configuration.setMaxQueued(Integer.valueOf(getValue(arg, "max.queued")));
			} else if (getValue(arg, "web.root") != null) {
				configuration.setWebRoot(getValue(arg, "web.root"));
			} else if (getValue(arg, "file.encoding") != null) {
				configuration.setFileEncoding(getValue(arg, "file.encoding"));
			} else if (getValue(arg, "uri.encoding") != null) {
				configuration.setUriEncoding(getValue(arg, "uri.encoding"));
			} else if (getValue(arg, "charset") != null) {
				configuration.setCharset(getValue(arg, "charset"));
			} else if (getValue(arg, "list.directory") != null) {
				configuration.setAllowListDirectory(Boolean.valueOf(getValue(arg, "list.directory")));
			} else if (getValue(arg, "index.pages") != null) {
				configuration.setIndexPages(StringUtils.split(getValue(arg, "index.pages"), ","));
			}
		}
		
		// 用绝对路径替换web root
		String webRoot = getAbsoluttePath(baseDirectory, configuration.getWebRoot());
		configuration.setWebRoot(webRoot);
		
		return configuration;
	}
	
	private String getAbsoluttePath(String base, String dir) {
		if (!isAbsolutePath(dir)) {
			dir = base + IOUtils.DIR_SEPARATOR + dir;
		}
		File file = new File(dir);
		return file.getAbsolutePath();
	}
	
	private boolean isAbsolutePath(String path) {
		if (StringUtils.startsWith(path, "/") || StringUtils.startsWith(path, "\\")) {
			return true;
		}
		
		if (IOUtils.DIR_SEPARATOR == '\\') {
			logger.debug("running on windows system");
			if (path.charAt(1) == ':') {
				return true;
			}
		}
		
		return false;
	}
	
	private String getValue(String arg, String prefix) {
		prefix = "--" + prefix + "=";
		if (StringUtils.startsWith(arg, prefix)) {
			return StringUtils.substring(arg, prefix.length());
		} else {
			return null;
		}
	}
}
