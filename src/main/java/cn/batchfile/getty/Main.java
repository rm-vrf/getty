package cn.batchfile.getty;

import org.apache.log4j.Logger;

import cn.batchfile.getty.boot.Server;
import cn.batchfile.getty.configuration.Configuration;

/**
 * Created by lane.cn on 14-10-27.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
    	String n = System.getProperty("line.seperator", "\r\n");
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
    	server.start(configuration);
    }
}
