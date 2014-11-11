package cn.batchfile.getty;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.boot.Server;
import cn.batchfile.getty.configuration.Configuration;

/**
 * Created by lane.cn on 14-10-27.
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
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
		Configuration configuration = new Configuration();
		
		//set base directory
		File file = new File(".");
		String path = file.getAbsolutePath();
		if (StringUtils.endsWith(path, File.separator + ".")) {
			path = StringUtils.substring(path, 0, path.length() - (File.separator + ".").length());
		}
		configuration.baseDirectory(path);
		
		server.start(configuration);
	}
}
