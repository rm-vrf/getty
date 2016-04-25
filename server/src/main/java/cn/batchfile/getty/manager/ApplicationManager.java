package cn.batchfile.getty.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationListener;
import cn.batchfile.getty.application.Cron;
import cn.batchfile.getty.application.Crontab;
import cn.batchfile.getty.application.ErrorHandler;
import cn.batchfile.getty.application.Filter;
import cn.batchfile.getty.application.Handler;
import cn.batchfile.getty.application.Session;
import cn.batchfile.getty.application.SessionListener;

public class ApplicationManager {
	
	private static final Logger logger = Logger.getLogger(ApplicationManager.class);
	private Map<String, Application> applications = new HashMap<String, Application>();

	public Application load(File dir) throws FileNotFoundException {

		Application application = new Application();
		application.setDir(dir);
		
		//解析app.yaml
		loadApplication(application, new File(dir, "app.yaml"));
		
		//解析cron.yaml
		loadCrontab(application, new File(dir, "cron.yaml"));
		
		//解析session.yaml
		loadSession(application, new File(dir, "session.yaml"));
		
		logger.info(String.format("load application from directory: %s, name: %s", dir, application.getName()));
		applications.put(application.getName(), application);
		return application;
	}
	
	public void unload(String name) {
		applications.remove(name);
	}
	
	@SuppressWarnings("unchecked")
	private void loadSession(Application application, File file) throws FileNotFoundException {
		Map<String, Object> map = (Map<String, Object>)Yaml.load(file);
		List<Map<String, Object>> list = null;
		
		application.setSession(new Session());
		if (map.containsKey("timeout")) {
			application.getSession().setTimeout(Integer.valueOf(map.get("timeout").toString()));
		}

		application.getSession().setListeners(new ArrayList<SessionListener>());
		if (map.containsKey("listeners")) {
			list = (List<Map<String, Object>>)map.get("listeners");
			for (Map<String, Object> element : list) {
				SessionListener listener = new SessionListener();
				if (element.containsKey("created")) {
					listener.setCreated(element.get("created").toString());
				}
				if (element.containsKey("destroyed")) {
					listener.setDestroyed(element.get("destroyed").toString());
				}
				application.getSession().getListeners().add(listener);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadCrontab(Application application, File file) throws FileNotFoundException {
		Map<String, Object> map = (Map<String, Object>)Yaml.load(file);
		List<Map<String, Object>> list = null;
		
		application.setCrontab(new Crontab());
		application.getCrontab().setCrons(new ArrayList<Cron>());
		if (map.containsKey("cron")) {
			list = (List<Map<String, Object>>)map.get("cron");
			for (Map<String, Object> element : list) {
				Cron cron = new Cron();
				if (element.containsKey("description")) {
					cron.setDescription(element.get("description").toString());
				}
				cron.setScript(element.get("script").toString());
				cron.setSchedule(element.get("schedule").toString());
				
				application.getCrontab().getCrons().add(cron);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void loadApplication(Application application, File file) throws FileNotFoundException {
		Map<String, Object> map = (Map<String, Object>)Yaml.load(file);
		List<Map<String, Object>> list = null;
		
		//基本属性
		application.setName(map.get("application").toString());
		if (map.containsKey("version")) {
			application.setVersion(map.get("version").toString());
		}
		if (map.containsKey("port")) {
			application.setPort(Integer.valueOf(map.get("port").toString()));
		}
		
		//控制器
		application.setHandlers(new ArrayList<Handler>());
		if (map.containsKey("handlers")) {
			list = (List<Map<String, Object>>)map.get("handlers");
			for (Map<String, Object> element : list) {
				Handler handler = new Handler();
				handler.setUrl(element.get("url").toString());
				if (element.containsKey("script")) {
					handler.setScript(element.get("script").toString());
				}
				if (element.containsKey("static_dir")) {
					handler.setStaticDir(element.get("static_dir").toString());
				}
				if (element.containsKey("static_files")) {
					handler.setStaticFiles(element.get("static_files").toString());
				}
				if (element.containsKey("http_headers")) {
					Map<String, String> headers = (Map<String, String>)element.get("http_headers");
					handler.setHttpHeaders(headers);
				}
				application.getHandlers().add(handler);
			}
		}
		
		//过滤器
		application.setFilters(new ArrayList<Filter>());
		if (map.containsKey("filters")) {
			list = (List<Map<String, Object>>)map.get("filters");
			for (Map<String, Object> element : list) {
				Filter filter = new Filter();
				filter.setUrlPattern(element.get("url_pattern").toString());
				filter.setScript(element.get("script").toString());
				
				application.getFilters().add(filter);
			}
		}
		
		//监听器
		application.setListeners(new ArrayList<ApplicationListener>());
		if (map.containsKey("listeners")) {
			list = (List<Map<String, Object>>)map.get("listeners");
			for (Map<String, Object> element : list) {
				ApplicationListener listener = new ApplicationListener();
				listener.setStart(element.get("start").toString());
				listener.setStop(element.get("stop").toString());
				
				application.getListeners().add(listener);
			}
		}
		
		//异常处理
		application.setErrorHandlers(new ArrayList<ErrorHandler>());
		if (map.containsKey("error_handlers")) {
			list = (List<Map<String, Object>>)map.get("error_handlers");
			for (Map<String, Object> element : list) {
				ErrorHandler handler = new ErrorHandler();
				if (element.containsKey("error_code")) {
					handler.setErrorCode(Integer.valueOf(element.get("error_code").toString()));
				}
				handler.setFile(element.get("file").toString());
				
				application.getErrorHandlers().add(handler);
			}
		}
	}
}
