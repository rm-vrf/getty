package cn.batchfile.getty.application;

import java.util.List;

public class Crontab {

	private boolean concurrent = false;
	private List<Cron> crons;

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public List<Cron> getCrons() {
		return crons;
	}

	public void setCrons(List<Cron> crons) {
		this.crons = crons;
	}
}
