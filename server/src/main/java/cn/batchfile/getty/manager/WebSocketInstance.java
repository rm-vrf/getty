package cn.batchfile.getty.manager;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.WebSocketHandler;
import cn.batchfile.getty.binding.socket.Cookie;
import cn.batchfile.getty.binding.socket.Request;
import cn.batchfile.getty.binding.socket.Response;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

@WebSocket
public class WebSocketInstance {

	private static final Logger LOG = Logger.getLogger(WebSocketInstance.class);
	private Map<String, GroovyScriptEngine> gses;
	private Application application;
	private ApplicationInstance applicationInstance;
	private ClassLoader classLoader;
	private Session session;
	private cn.batchfile.getty.binding.socket.Session bindingSession;

	public Map<String, GroovyScriptEngine> getGses() {
		return gses;
	}

	public void setGses(Map<String, GroovyScriptEngine> gses) {
		this.gses = gses;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public ApplicationInstance getApplicationInstance() {
		return applicationInstance;
	}

	public void setApplicationInstance(ApplicationInstance applicationInstance) {
		this.applicationInstance = applicationInstance;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	@OnWebSocketConnect
    public void handleConnect(Session session) {
		String uri = session.getUpgradeRequest().getRequestURI().getPath();
		LOG.debug("websocket connect: " + uri);
		
		//create session
		this.session = session;
		this.bindingSession = new cn.batchfile.getty.binding.socket.Session(session);
		
		//find hanlder
		WebSocketHandler handler = getHandler(uri);
		if (handler == null) {
			session.getUpgradeResponse().setStatusCode(404);
			return;
		}
		
		//get file
		String file = handler.getConnect();
		if (StringUtils.isEmpty(file)) {
			return;
		}
		
		bindAndRun(handler, file, null, null);
    }
	
	@OnWebSocketClose
    public void handleClose(int statusCode, String reason) throws IOException {
		String uri = session.getUpgradeRequest().getRequestURI().getPath();
		LOG.debug("websocket close: " + uri);
		
		//set close info
		session.getUpgradeResponse().setStatusCode(statusCode);
		session.getUpgradeResponse().setStatusReason(reason);
		
		//find hanlder
		WebSocketHandler handler = getHandler(uri);
		if (handler == null) {
			session.getUpgradeResponse().setStatusCode(404);
			return;
		}
		
		//get file
		String file = handler.getClose();
		if (StringUtils.isEmpty(file)) {
			session.disconnect();
			return;
		}
		
		bindAndRun(handler, file, null, null);
		session.disconnect();
    }
	
	@OnWebSocketMessage
    public void handleMessage(String message) {
		String uri = session.getUpgradeRequest().getRequestURI().getPath();
		LOG.debug("websocket message: " + uri);
		
		//find hanlder
		WebSocketHandler handler = getHandler(uri);
		if (handler == null) {
			session.getUpgradeResponse().setStatusCode(404);
			return;
		}
		
		//get file
		String file = handler.getMessage();
		if (StringUtils.isEmpty(file)) {
			return;
		}
		
		bindAndRun(handler, file, message, null);
    }
	
	@OnWebSocketError
    public void handleError(Throwable error) {
		String uri = session.getUpgradeRequest().getRequestURI().getPath();
		LOG.debug("websocket message: " + uri);
		
		//find hanlder
		WebSocketHandler handler = getHandler(uri);
		if (handler == null) {
			session.getUpgradeResponse().setStatusCode(404);
			return;
		}
		
		//get file
		String file = handler.getError();
		if (StringUtils.isEmpty(file)) {
			return;
		}
		
		bindAndRun(handler, file, null, error);
    }
	
	private void bindAndRun(WebSocketHandler handler, String file, String message, Throwable t) {
		//create binding object
		Request bindingRequest = new Request(session, message);
		Response bindingResponse = new Response(session);
		Cookie bindingCookie = new Cookie(session.getUpgradeRequest().getCookies());
		Logger bindingLogger = Logger.getLogger(file);
		
		//binding inner object
		Binding binding = new Binding();
		binding.setProperty("$application", applicationInstance);
		binding.setProperty("$app", applicationInstance);
		binding.setProperty("$request", bindingRequest);
		binding.setProperty("$req", bindingRequest);
		binding.setProperty("$response", bindingResponse);
		binding.setProperty("$res", bindingResponse);
		binding.setProperty("$resp", bindingResponse);
		binding.setProperty("$session", bindingSession);
		binding.setProperty("$cookie", bindingCookie);
		binding.setProperty("$logger", bindingLogger);
		binding.setProperty("$log", bindingLogger);
		if (t != null) {
			binding.setProperty("$error", t);
			binding.setProperty("$e", t);
		}
		if (message != null) {
			binding.setProperty("$message", message);
		}
		
		//binding input param
		for (Entry<String, Object> entry : bindingRequest.getParameters().entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}
		
		//set response charset
		bindingResponse.setCharset(application.getCharsetEncoding());
		
		//execute script file
		try {
			GroovyScriptEngine gse = getGroovyScriptEngine(application);
			Object r = gse.run(file, binding);
			if (LOG.isDebugEnabled()) {
				LOG.debug("shell return value: " + r);
			}
		} catch (IOException e) {
			throw new RuntimeException("script exception: " + file, e);
		} catch (ResourceException e) {
			throw new RuntimeException("script exception: " + file, e);
		} catch (ScriptException e) {
			throw new RuntimeException("script exception: " + file, e);
		}
		
		//process content-type
		if (StringUtils.isEmpty(bindingResponse.getContentType())) {
			bindingResponse.setContentType("text/html");
		}
	}

	private GroovyScriptEngine getGroovyScriptEngine(Application application) throws IOException {
		String key = application.getDir().getAbsolutePath();
		if (!gses.containsKey(key)) {
			synchronized (gses) {
				if (!gses.containsKey(key)) {
					String path = application.getDir().getAbsolutePath();
					GroovyScriptEngine gse = new GroovyScriptEngine(path, classLoader);
					gses.put(key, gse);
				}
			}
		}
		return gses.get(key);
	}
	
	private WebSocketHandler getHandler(String uri) {
		for (WebSocketHandler handler : application.getWebSocket().getHandlers()) {
			if (StringUtils.equals(uri, handler.getUrl())) {
				return handler;
			}
		}
		return null;
	}
}
