package cn.batchfile.getty.application;

public class Cron {

	private String description;
	private String script;
	private String schedule;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getScript() {
		return script;
	}
	
	public void setScript(String script) {
		this.script = script;
	}
	
	public String getSchedule() {
		return schedule;
	}
	
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
}
