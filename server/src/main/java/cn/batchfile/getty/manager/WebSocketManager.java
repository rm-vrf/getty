package cn.batchfile.getty.manager;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebSocket
public class WebSocketManager extends WebSocketServlet {

	private static final long serialVersionUID = 6471275025352138121L;
	private Session session;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(getClass());
	}

	@OnWebSocketConnect
    public void handleConnect(Session session) {
		System.out.println("Connection opened with requestURI=" + session.getUpgradeRequest().getRequestURI());
        this.session = session;
    }
	
	@OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        System.out.println("Connection closed with statusCode=" + statusCode + ", reason=" + reason);
    }
	
	@OnWebSocketMessage
    public void handleMessage(String message) {
        switch (message) {
            case "start":
                send("Temperature service started!");
                executor.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						String message = TemperatureService.getTemperatureInfo();
						send(message);
					}
				}, 0, 5, TimeUnit.SECONDS);
                break;
            case "stop":
                this.stop();
                break;
        }
    }
	
	@OnWebSocketError
    public void handleError(Throwable error) {
        error.printStackTrace();
    }
	
	private void send(String message) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void stop() {
        try {
            session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	static class TemperatureService {
		 
	    // Random value generators. One for each sample
	    private static Random rnd = new Random();
	    private static Iterator<Double> hf = rnd.doubles(13, 29).iterator();
	    private static Iterator<Double> bj = rnd.doubles(11, 27).iterator();
	    private static Iterator<Double> xa = rnd.doubles(14, 32).iterator();
	    private static Iterator<Double> cd = rnd.doubles(17, 32).iterator();
	 
	    private static final String FORMAT =
	            "[{0}]\t\tHefei = {1, number, #0.00}\tBeijing = {2, number, #0.00}" +
	                    "\tXian = {3, number, #0.00}\tChengdu = {4, number, #0.00}";
	 
	    public static String getTemperatureInfo() {
	        return MessageFormat.format(FORMAT, LocalTime.now().toString(),
	                hf.next(), bj.next(), xa.next(), cd.next());
	    }
	}
}
