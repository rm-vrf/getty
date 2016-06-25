package cn.batchfile.getty.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.ApplicationListener;

public class ApplicationEventListener {

	private static final Logger LOG = Logger.getLogger(ApplicationEventListener.class);
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
	
	public void applicationStart() {
		execute(true);	
	}
	
	public void applicationStop() {
		execute(false);
	}
	
	private void execute(boolean start) {
		//创建脚本变量
		Map<String, Object> binding = new HashMap<String, Object>();
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);

		//如果设置了监听器，执行每一个创建脚本
		if (application.getListeners() != null) {
			for (ApplicationListener listener : application.getListeners()) {
				
				//判断脚本的类型
				String file = start ? listener.getStart() : listener.getStop();
				
				//如果设置了脚本名称，执行它
				if (StringUtils.isNotBlank(file)) {
					try {
						Logger bindingLogger = Logger.getLogger(file);
						binding.put("$logger", bindingLogger);
						binding.put("$log", bindingLogger);
						scriptEngineManager.run(file, binding);
					} catch (Exception e) {
						LOG.error("error when execute application listener: " + file, e);
					}
				}
			}
		}
	}
}
