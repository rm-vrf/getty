package cn.batchfile.getty;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.boot.Server;
import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.util.Log4jConfigurator;

/**
 * Created by lane.cn on 14-10-27.
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		Configuration configuration = new Main().getConfiguration(args);
		
		new Log4jConfigurator().load(configuration);
		
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

		Server server = new Server();
		server.start(configuration);
	}
	
	private Configuration getConfiguration(String[] args) {
		Configuration configuration = new Configuration();
		
		//set base directory
		File file = new File(".");
		String path = file.getAbsolutePath();
		if (StringUtils.endsWith(path, File.separator + ".")) {
			path = StringUtils.substring(path, 0, path.length() - (File.separator + ".").length());
		}
		configuration.baseDirectory(path);
		
		//set args
		for (String arg : args) {
			if (getValue(arg, "port") != null) {
				configuration.port(Integer.valueOf(getValue(arg, "port")));
			} else if (getValue(arg, "max.thread") != null) {
				configuration.maxThread(Integer.valueOf(getValue(arg, "max.thread")));
			} else if (getValue(arg, "min.thread") != null) {
				configuration.minThread(Integer.valueOf(getValue(arg, "min.thread")));
			} else if (getValue(arg, "max.idle") != null) {
				configuration.maxIdleTime(Integer.valueOf(getValue(arg, "max.idle")));
			} else if (getValue(arg, "log.level") != null) {
				configuration.logLevel(getValue(arg, "log.level"));
			} else if (getValue(arg, "max.queued") != null) {
				configuration.maxQueued(Integer.valueOf(getValue(arg, "max.queued")));
			} else if (getValue(arg, "web.root") != null) {
				configuration.webRoot(getValue(arg, "web.root"));
			} else if (getValue(arg, "file.encoding") != null) {
				configuration.fileEncoding(getValue(arg, "file.encoding"));
			} else if (getValue(arg, "uri.encoding") != null) {
				configuration.uriEncoding(getValue(arg, "uri.encoding"));
			} else if (getValue(arg, "charset") != null) {
				configuration.charset(getValue(arg, "charset"));
			} else if (getValue(arg, "list.directory") != null) {
				configuration.allowListDirectory(Boolean.valueOf(getValue(arg, "list.directory")));
			} else if (getValue(arg, "index.pages") != null) {
				configuration.indexPages(StringUtils.split(getValue(arg, "index.pages"), ","));
			}
		}
		
		return configuration;
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
