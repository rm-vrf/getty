package cn.batchfile.getty.application;

import java.util.List;

public class WebSocket {
	private String urlPattern;
	private List<WebSocketHandler> handlers;

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public List<WebSocketHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<WebSocketHandler> handlers) {
		this.handlers = handlers;
	}
}
