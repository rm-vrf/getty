package cn.batchfile.getty.configuration;

import org.apache.commons.lang.StringUtils;

/**
 * 系统基本设置
 * @author htlu
 *
 */
public class Configuration {
	private int port = 1025;
	private int maxThread = -1;
	private int minThread = -1;
	private int maxIdleTime = -1;
	private String logLevel = "INFO";
	private String baseDirectory;
	private String contextPath = StringUtils.EMPTY;
	private int requestHeaderSize = -1;
	private int maxQueued = 1;
	private String webRoot = "/webapp";
	private String fileEncoding = "UTF-8";
	private String charset = "UTF-8";
	private String uriEncoding = "UTF-8";
	private boolean allowListDirectory = true;
	private String[] indexPages = new String[] {
		"index.html", 
		"index.htm", 
		"index.groovy"
	};

	/**
	 * 端口
	 * @return 端口
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * 端口
	 * @param port 端口
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * 最大线程数
	 * @return 最大线程数
	 */
	public int getMaxThread() {
		return maxThread;
	}
	
	/**
	 * 最大线程数
	 * @param maxThread 最大线程数
	 */
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	
	/**
	 * 最小线程数
	 * @return 最小线程数
	 */
	public int getMinThread() {
		return minThread;
	}
	
	/**
	 * 最小线程数
	 * @param minThread 最小线程数
	 */
	public void setMinThread(int minThread) {
		this.minThread = minThread;
	}

	/**
	 * 线程空闲时间
	 * @return 线程空闲时间
	 */
	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	/**
	 * 线程空闲时间
	 * @param maxIdleTime 线程空闲时间
	 */
	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	/**
	 * 日志级别
	 * @return 日志级别
	 */
	public String getLogLevel() {
		return logLevel;
	}

	/**
	 * 日志级别
	 * @param logLevel 日志级别
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * 根目录
	 * @return 根目录
	 */
	public String getBaseDirectory() {
		return baseDirectory;
	}

	/**
	 * 根目录
	 * @param baseDirectory 根目录
	 */
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	/**
	 * Web Context
	 * @return Web Context
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * Web Context
	 * @param contextPath Web Context
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Request Header Size
	 * @return Request Header Size
	 */
	public int getRequestHeaderSize() {
		return requestHeaderSize;
	}

	/**
	 * Request Header Size
	 * @param requestHeaderSize Request Header Size
	 */
	public void setRequestHeaderSize(int requestHeaderSize) {
		this.requestHeaderSize = requestHeaderSize;
	}

	/**
	 * Max Queued Messages
	 * @return Max Queued Messages
	 */
	public int getMaxQueued() {
		return maxQueued;
	}

	/**
	 * Max Queued Messages
	 * @param maxQueued Max Queued Messages
	 */
	public void setMaxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
	}

	/**
	 * Web Root
	 * @return Web Root
	 */
	public String getWebRoot() {
		return webRoot;
	}

	/**
	 * Web Root
	 * @param webRoot Web Root
	 */
	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	/**
	 * File encoding
	 * @return File encoding
	 */
	public String getFileEncoding() {
		return fileEncoding;
	}

	/**
	 * File encoding
	 * @param fileEncoding File encoding
	 */
	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	/**
	 * Charset
	 * @return Charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Charset
	 * @param charset Charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * URI Encoding
	 * @return URI Encoding
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * URI Encoding
	 * @param uriEncoding URI Encoding
	 */
	public void setUriEncoding(String uriEncoding) {
		this.uriEncoding = uriEncoding;
	}

	/**
	 * Allow List Directory
	 * @return Allow List Directory
	 */
	public boolean isAllowListDirectory() {
		return allowListDirectory;
	}

	/**
	 * Allow List Directory
	 * @param allowListDirectory Allow List Directory
	 */
	public void setAllowListDirectory(boolean allowListDirectory) {
		this.allowListDirectory = allowListDirectory;
	}

	/**
	 * Index Pages
	 * @return Index Pages
	 */
	public String[] getIndexPages() {
		return indexPages;
	}

	/**
	 * Index Pages
	 * @param indexPages Index Pages
	 */
	public void setIndexPages(String[] indexPages) {
		this.indexPages = indexPages;
	}
}
