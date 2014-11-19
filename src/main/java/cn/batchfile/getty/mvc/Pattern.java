package cn.batchfile.getty.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pattern {
	private String pattern;
	private List<String> methods = new ArrayList<String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private String uri;
	private List<String> parts = new ArrayList<String>();
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public List<String> getMethods() {
		return methods;
	}
	
	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<String> getParts() {
		return parts;
	}

	public void setParts(List<String> parts) {
		this.parts = parts;
	}
}
