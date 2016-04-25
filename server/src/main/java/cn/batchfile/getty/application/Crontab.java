package cn.batchfile.getty.application;

import java.util.List;

public class Crontab {

	private List<Cron> crons;

	public List<Cron> getCrons() {
		return crons;
	}

	public void setCrons(List<Cron> crons) {
		this.crons = crons;
	}
}
