package cn.batchfile.getty.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.Handler;

public class MappingManager {

	private static final Logger logger = Logger.getLogger(MappingManager.class);
	private Application application;
	private List<HandlerPattern> patterns = new ArrayList<HandlerPattern>();
	
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
		
		patterns.clear();
		for (Handler handler : application.getHandlers()) {
			HandlerPattern hp = new HandlerPattern();
			hp.handler = handler;
			hp.parts = split(handler.getUrl(), '/');
			patterns.add(hp);
		}
	}

	public Handler mapping(String url, Map<String, Object> vars) {
		List<String> parts = split(url, '/');
		for (HandlerPattern pattern : patterns) {
			vars.clear();
			if (match(pattern.parts, parts, vars)) {
				logger.debug("match url handler: " + pattern.handler.getUrl());
				return pattern.handler;
			}
		}
		return null;
	}
	
	private boolean match(List<String> urlParts, List<String> requestParts, Map<String, Object> vars) {
		if (urlParts.size() != requestParts.size()) {
			return false;
		}
		
		for (int i = 0; i < urlParts.size(); i ++) {
			String urlPart = urlParts.get(i);
			String requestPart = requestParts.get(i);
			
			if (StringUtils.startsWith(urlPart, "{") && StringUtils.endsWith(urlPart, "}")) {
				vars.put(StringUtils.substringBetween(urlPart, "{", "}"), requestPart);
			} else if (!StringUtils.equals(urlPart, requestPart)) {
				return false;
			}
		}
		
		return true;
	}

	private List<String> split(String s, char c) {
		String[] ary = StringUtils.split(s, c);
		List<String> l = new ArrayList<String>();
		for (String element : ary) {
			if (StringUtils.isNotEmpty(element)) {
				l.add(element);
			}
		}
		return l;
	}
	
	class HandlerPattern {
		Handler handler;
		List<String> parts;
	}
}
