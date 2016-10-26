package cn.batchfile.getty.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.SessionListener;
import cn.batchfile.getty.binding.http.Session;

public class SessionEventListener implements HttpSessionListener {

	private static final Logger LOG = Logger.getLogger(SessionEventListener.class);
	private Application application;
	private ApplicationInstance applicationInstance;
	private ScriptEngineManager scriptEngineManager;
	
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
		execute(event.getSession(), true);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		execute(event.getSession(), false);
	}

	private void execute(HttpSession session, boolean created) {
		//创建会话对象
		Session bindingSession = new Session(session);
		
		//创建脚本变量
		Map<String, Object> binding = new HashMap<String, Object>();
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);
		binding.put("$session", bindingSession);
		
		//如果设置了监听器，执行每一个创建脚本
		if (application.getSession() != null && application.getSession().getListeners() != null) {
			for (SessionListener listener : application.getSession().getListeners()) {

				//判断脚本的类型
				String file = created ? listener.getCreated() : listener.getDestroyed();
				
				//如果设置了脚本名称，执行它
				if (StringUtils.isNotBlank(file)) {
					try {
						Logger bindingLogger = Logger.getLogger(file);
						binding.put("$logger", bindingLogger);
						binding.put("$log", bindingLogger);
						scriptEngineManager.runFile(file, binding);
					} catch (Exception e) {
						LOG.error("error when execute session listener: " + file, e);
					}
				}
			}
		}
	}
	
}
