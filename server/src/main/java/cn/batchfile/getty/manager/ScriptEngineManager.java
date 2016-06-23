package cn.batchfile.getty.manager;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class ScriptEngineManager {
	
	private static final Logger LOG = Logger.getLogger(ScriptEngineManager.class);
	private GroovyScriptEngine scriptEngine;
	private Application application;
	private ClassLoader classLoader;
	
	public Application getApplication() {
		return application;
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public Object run(String file, Map<String, Object> vars) {
		Binding binding = new Binding();
		for (Entry<String, Object> var : vars.entrySet()) {
			binding.setProperty(var.getKey(), var.getValue());
		}
		
		try {
			if (scriptEngine == null) {
				synchronized (this) {
					if (scriptEngine == null) {
						String path = application.getDir().getAbsolutePath();
						scriptEngine = new GroovyScriptEngine(path, classLoader);
					}
				}
			}
			
			Object r = scriptEngine.run(file, binding);
			if (LOG.isDebugEnabled()) {
				LOG.debug("shell return value: " + r);
			}
			return r;
		} catch (IOException e) {
			throw new cn.batchfile.getty.exceptions.ScriptException("script exception: " + file, e);
		} catch (ResourceException e) {
			throw new cn.batchfile.getty.exceptions.ScriptException("script exception: " + file, e);
		} catch (ScriptException e) {
			throw new cn.batchfile.getty.exceptions.ScriptException("script exception: " + file, e);
		}
	}
}
