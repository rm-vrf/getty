package cn.batchfile.getty.manager;

import java.io.IOException;
import java.util.HashMap;
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

@WebSocket
public class WebSocketInstance {

	private static final Logger LOG = Logger.getLogger(WebSocketInstance.class);
	private ScriptEngineManager scriptEngineManager;
	private Application application;
	private ApplicationInstance applicationInstance;
	private ClassLoader classLoader;
	private Session session;
	private cn.batchfile.getty.binding.socket.Session bindingSession;

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
	
	public ScriptEngineManager getScriptEngineManager() {
		return scriptEngineManager;
	}

	public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
		this.scriptEngineManager = scriptEngineManager;
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
		Map<String, Object> binding = new HashMap<String, Object>();
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);
		binding.put("$request", bindingRequest);
		binding.put("$req", bindingRequest);
		binding.put("$response", bindingResponse);
		binding.put("$res", bindingResponse);
		binding.put("$resp", bindingResponse);
		binding.put("$session", bindingSession);
		binding.put("$cookie", bindingCookie);
		binding.put("$logger", bindingLogger);
		binding.put("$log", bindingLogger);
		if (t != null) {
			binding.put("$error", t);
			binding.put("$e", t);
		}
		if (message != null) {
			binding.put("$message", message);
		}
		
		//binding input param
		for (Entry<String, Object> entry : bindingRequest.getParameters().entrySet()) {
			binding.put(entry.getKey(), entry.getValue());
		}
		
		//set response charset
		bindingResponse.setCharset(application.getCharsetEncoding());
		
		//execute script file
		Object r = scriptEngineManager.run(file, binding);
		if (LOG.isDebugEnabled()) {
			LOG.debug("shell return value: " + r);
		}
		
		//process content-type
		if (StringUtils.isEmpty(bindingResponse.getContentType())) {
			bindingResponse.setContentType("text/html");
		}
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
