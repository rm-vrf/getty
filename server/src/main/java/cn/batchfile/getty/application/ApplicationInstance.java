package cn.batchfile.getty.application;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import cn.batchfile.getty.util.DateSerializer;

public class ApplicationInstance {

	private Application application;
	private int port = 0;
	private Date startTime;
	private Map<String, Object> attributes;
	//private ClassLoader classLoader;
	
	public ApplicationInstance() {
		attributes = new ConcurrentHashMap<String, Object>();
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

//	public ClassLoader getClassLoader() {
//		return classLoader;
//	}
//
//	public void setClassLoader(ClassLoader classLoader) {
//		this.classLoader = classLoader;
//	}
}
