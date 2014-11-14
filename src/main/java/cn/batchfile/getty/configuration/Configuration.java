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
	private String webRoot = "webapp";
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
	public int port() {
		return port;
	}
	
	/**
	 * 端口
	 * @param port 端口
	 * @return {@link Configuration}
	 */
	public Configuration port(int port) {
		this.port = port;
		return this;
	}
	
	/**
	 * 最大线程数
	 * @return 最大线程数
	 */
	public int maxThread() {
		return maxThread;
	}
	
	/**
	 * 最大线程数
	 * @param maxThread 最大线程数
	 * @return {@link Configuration}
	 */
	public Configuration maxThread(int maxThread) {
		this.maxThread = maxThread;
		return this;
	}
	
	/**
	 * 最小线程数
	 * @return 最小线程数
	 */
	public int minThread() {
		return minThread;
	}
	
	/**
	 * 最小线程数
	 * @param minThread 最小线程数
	 * @return {@link Configuration}
	 */
	public Configuration minThread(int minThread) {
		this.minThread = minThread;
		return this;
	}

	/**
	 * 线程空闲时间
	 * @return 线程空闲时间
	 */
	public int maxIdleTime() {
		return maxIdleTime;
	}

	/**
	 * 线程空闲时间
	 * @param maxIdleTime 线程空闲时间
	 * @return {@link Configuration}
	 */
	public Configuration maxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
		return this;
	}

	/**
	 * 日志级别
	 * @return 日志级别
	 */
	public String logLevel() {
		return logLevel;
	}

	/**
	 * 日志级别
	 * @param logLevel 日志级别
	 * @return {@link Configuration}
	 */
	public Configuration logLevel(String logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	/**
	 * 根目录
	 * @return 根目录
	 */
	public String baseDirectory() {
		return baseDirectory;
	}

	/**
	 * 根目录
	 * @param baseDirectory 根目录
	 * @return {@link Configuration}
	 */
	public Configuration baseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
		return this;
	}

	/**
	 * Web Context
	 * @return Web Context
	 */
	public String contextPath() {
		return contextPath;
	}

	/**
	 * Web Context
	 * @param contextPath Web Context
	 * @return {@link Configuration}
	 */
	public Configuration contextPath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	/**
	 * Request Header Size
	 * @return Request Header Size
	 */
	@Deprecated
	public int requestHeaderSize() {
		return requestHeaderSize;
	}

	/**
	 * Request Header Size
	 * @param requestHeaderSize Request Header Size
	 * @return {@link Configuration}
	 */
	@Deprecated
	public Configuration requestHeaderSize(int requestHeaderSize) {
		this.requestHeaderSize = requestHeaderSize;
		return this;
	}

	/**
	 * Max Queued Messages
	 * @return Max Queued Messages
	 */
	public int maxQueued() {
		return maxQueued;
	}

	/**
	 * Max Queued Messages
	 * @param maxQueued Max Queued Messages
	 * @return {@link Configuration}
	 */
	public Configuration maxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
		return this;
	}

	/**
	 * Web Root
	 * @return Web Root
	 */
	public String webRoot() {
		return webRoot;
	}

	/**
	 * Web Root
	 * @param webRoot Web Root
	 * @return {@link Configuration}
	 */
	public Configuration webRoot(String webRoot) {
		this.webRoot = webRoot;
		return this;
	}

	/**
	 * File encoding
	 * @return File encoding
	 */
	public String fileEncoding() {
		return fileEncoding;
	}

	/**
	 * File encoding
	 * @param fileEncoding File encoding
	 * @return {@link Configuration}
	 */
	public Configuration fileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
		return this;
	}

	/**
	 * Charset
	 * @return Charset
	 */
	public String charset() {
		return charset;
	}

	/**
	 * Charset
	 * @param charset Charset
	 * @return {@link Configuration}
	 */
	public Configuration charset(String charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * URI Encoding
	 * @return URI Encoding
	 */
	public String uriEncoding() {
		return uriEncoding;
	}

	/**
	 * URI Encoding
	 * @param uriEncoding URI Encoding
	 * @return {@link Configuration}
	 */
	public Configuration uriEncoding(String uriEncoding) {
		this.uriEncoding = uriEncoding;
		return this;
	}

	/**
	 * Allow List Directory
	 * @return Allow List Directory
	 * @return {@link Configuration}
	 */
	public boolean allowListDirectory() {
		return allowListDirectory;
	}

	/**
	 * Allow List Directory
	 * @param allowListDirectory Allow List Directory
	 */
	public Configuration allowListDirectory(boolean allowListDirectory) {
		this.allowListDirectory = allowListDirectory;
		return this;
	}

	/**
	 * Index Pages
	 * @return Index Pages
	 * @return {@link Configuration}
	 */
	public String[] indexPages() {
		return indexPages;
	}

	/**
	 * Index Pages
	 * @param indexPages Index Pages
	 * @return {@link Configuration}
	 */
	public Configuration indexPages(String[] indexPages) {
		this.indexPages = indexPages;
		return this;
	}
}
