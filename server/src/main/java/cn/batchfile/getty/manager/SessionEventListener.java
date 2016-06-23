package cn.batchfile.getty.manager;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.SessionListener;

public class SessionEventListener implements HttpSessionListener {

	private static final Logger LOG = Logger.getLogger(SessionEventListener.class);
	private List<SessionListener> sessionListeners;
	private Application application;
	private ApplicationInstance applicationInstance;
	private ScriptEngineManager scriptEngineManager;
	
	public List<SessionListener> getSessionListeners() {
		return sessionListeners;
	}

	public void setSessionListeners(List<SessionListener> sessionListeners) {
		this.sessionListeners = sessionListeners;
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

	public ScriptEngineManager getScriptEngineManager() {
		return scriptEngineManager;
	}

	public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
		this.scriptEngineManager = scriptEngineManager;
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		LOG.debug("create session, id: " + event.getSession().getId());
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		LOG.debug("destroy session, id: " + event.getSession().getId());
		
		// TODO Auto-generated method stub
		
	}

}
