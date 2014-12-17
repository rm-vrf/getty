package cn.batchfile.getty.configuration;

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
	public int getPort() {
		return port;
	}
	
	/**
	 * 端口
	 * @param port 端口
	 * @return {@link Configuration}
	 */
	public Configuration setPort(int port) {
		this.port = port;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setMaxThread(int maxThread) {
		this.maxThread = maxThread;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setMinThread(int minThread) {
		this.minThread = minThread;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setLogLevel(String logLevel) {
		this.logLevel = logLevel;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setMaxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setWebRoot(String webRoot) {
		this.webRoot = webRoot;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setCharset(String charset) {
		this.charset = charset;
		return this;
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
	 * @return {@link Configuration}
	 */
	public Configuration setUriEncoding(String uriEncoding) {
		this.uriEncoding = uriEncoding;
		return this;
	}

	/**
	 * Allow List Directory
	 * @return Allow List Directory
	 * @return {@link Configuration}
	 */
	public boolean getAllowListDirectory() {
		return allowListDirectory;
	}

	/**
	 * Allow List Directory
	 * @param allowListDirectory Allow List Directory
	 */
	public Configuration setAllowListDirectory(boolean allowListDirectory) {
		this.allowListDirectory = allowListDirectory;
		return this;
	}

	/**
	 * Index Pages
	 * @return Index Pages
	 * @return {@link Configuration}
	 */
	public String[] getIndexPages() {
		return indexPages;
	}

	/**
	 * Index Pages
	 * @param indexPages Index Pages
	 * @return {@link Configuration}
	 */
	public Configuration setIndexPages(String[] indexPages) {
		this.indexPages = indexPages;
		return this;
	}
}
