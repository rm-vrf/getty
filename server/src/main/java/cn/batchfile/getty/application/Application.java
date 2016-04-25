package cn.batchfile.getty.application;

import java.io.File;
import java.util.List;

public class Application {

	private File dir;
	private String name;
	private String version;
	private int port = 0;
	private List<Handler> handlers;
	private List<Filter> filters;
	private List<ApplicationListener> listeners;
	private List<ErrorHandler> errorHandlers;
	private Crontab crontab;
	private Session session;
	
	public File getDir() {
		return dir;
	}
	
	public void setDir(File dir) {
		this.dir = dir;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public List<Handler> getHandlers() {
		return handlers;
	}
	
	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}
	
	public List<Filter> getFilters() {
		return filters;
	}
	
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	public List<ApplicationListener> getListeners() {
		return listeners;
	}
	
	public void setListeners(List<ApplicationListener> listeners) {
		this.listeners = listeners;
	}
	
	public List<ErrorHandler> getErrorHandlers() {
		return errorHandlers;
	}
	
	public void setErrorHandlers(List<ErrorHandler> errorHandlers) {
		this.errorHandlers = errorHandlers;
	}

	public Crontab getCrontab() {
		return crontab;
	}

	public void setCrontab(Crontab crontab) {
		this.crontab = crontab;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
