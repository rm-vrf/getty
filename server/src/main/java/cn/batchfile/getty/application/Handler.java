package cn.batchfile.getty.application;

import java.util.Map;

public class Handler {

	private String url;
	private String script;
	private String staticDir;
	private String staticFiles;
	private Map<String, String> httpHeaders;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getStaticDir() {
		return staticDir;
	}

	public void setStaticDir(String staticDir) {
		this.staticDir = staticDir;
	}

	public String getStaticFiles() {
		return staticFiles;
	}

	public void setStaticFiles(String staticFiles) {
		this.staticFiles = staticFiles;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
}
