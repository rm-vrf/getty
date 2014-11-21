package cn.batchfile.getty.binding;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.batchfile.getty.configuration.Configuration;

public class Application {

	private static Application instance = new Application();
	private Date startTime;
	private Map<String, Object> attributes;
	private Configuration configuration;
	private String baseDirectory;
	
	public static Application getInstance() {
		return instance;
	}
	
	private Application() {
		attributes = new ConcurrentHashMap<String, Object>();
	}
	
	public String getName() {
		return "getty";
	}
	
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	public Date getBuildTime() {
		return new Date(0);
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public String getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
