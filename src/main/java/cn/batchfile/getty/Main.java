package cn.batchfile.getty;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by lane.cn on 14-10-27.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Start up getty");

        final Server server = new Server(1025);
        ServletContextHandler handler = new ServletContextHandler();
        //handler.addServlet();

        server.setStopTimeout(1000 * 20);
        server.setStopAtShutdown(true);
        server.setHandler(handler);

        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        server.start();
                        server.join();
                    } catch (Exception e) {
                        logger.error("Error when start getty", e);
                    }
                }
            }).start();
        } catch (Exception e) {
            logger.error("Error when start getty", e);
        }
    }
}
