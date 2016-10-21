package cn.batchfile.getty.application;

public class ThreadPool {
	private int maxThreads = -1;
	private int minThreads = -1;
	private int idleTimeout = -1;

	public int getMaxThreads() {
		return maxThreads;
	}
	
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	
	public int getMinThreads() {
		return minThreads;
	}
	
	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}
	
	public int getIdleTimeout() {
		return idleTimeout;
	}
	
	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}	
}
