package cn.batchfile.getty.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.Cron;

public class CrontabManager {

	private static final Logger LOG = Logger.getLogger(CrontabManager.class);
	private Application application;
	private ApplicationInstance applicationInstance;
	private ScriptEngineManager scriptEngineManager;
	private SchedulerFactoryBean schedulerFactory;
	
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public ApplicationInstance getApplicationInstance() {
		return applicationInstance;
	}

	public void setApplicationInstance(ApplicationInstance applicationInstance) {
		this.applicationInstance = applicationInstance;
	}

	public ScriptEngineManager getScriptEngineManager() {
		return scriptEngineManager;
	}

	public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
		this.scriptEngineManager = scriptEngineManager;
	}

	public void start() throws Exception {
		LOG.info("start crontab");
		
		if (application.getCrontab() != null && application.getCrontab().getCrons() != null) {
			
			Trigger[] triggers = new Trigger[application.getCrontab().getCrons().size()];
			for (int i = 0; i < triggers.length; i ++) {
				
				Cron cron = application.getCrontab().getCrons().get(i);
				String desc = cron.getDescription();
				String exp = cron.getSchedule();
				String script = cron.getScript();
				
				MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
				jobDetailFactory.setBeanName("jobDetail" + script);
				jobDetailFactory.setName("jobDetail" + script);
				jobDetailFactory.setTargetObject(this);
				jobDetailFactory.setTargetMethod("execute");
				jobDetailFactory.setArguments(new Object[]{script, exp, desc});
				jobDetailFactory.setConcurrent(application.getCrontab().isConcurrent());
				jobDetailFactory.afterPropertiesSet();
				
				JobDetail jobDetail = jobDetailFactory.getObject();

				CronTriggerBean trigger = new CronTriggerBean();
				trigger.setBeanName("trigger" + script);
				trigger.setName("trigger" + script);
				trigger.setJobDetail(jobDetail);
				trigger.setCronExpression(exp);
				trigger.afterPropertiesSet();
				
				triggers[i] = trigger;
			}

			Properties properties = new Properties();
			properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
			
			schedulerFactory = new SchedulerFactoryBean();
			schedulerFactory.setBeanName("schedulerFactory");
			schedulerFactory.setTriggers(triggers);
			schedulerFactory.setQuartzProperties(properties);
			//schedulerFactory.setAutoStartup(true);
			schedulerFactory.afterPropertiesSet();
			schedulerFactory.start();
		}
	}
	
	public void stop() {
		LOG.info("stop crontab");
		try {
			schedulerFactory.stop();
		} catch (Exception e) {
			//do nothing
		}
		try {
			schedulerFactory.destroy();
		} catch (SchedulerException e) {
			//do nothing
		}
	}

	public void execute(String file, String expression, String description) {
		LOG.debug("execute crontab: " + file + ", " + expression + ", " + description);

		Logger bindingLogger = Logger.getLogger(file);
		
		Map<String, Object> binding = new HashMap<String, Object>();
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);
		binding.put("$logger", bindingLogger);
		binding.put("$log", bindingLogger);

		scriptEngineManager.run(file, binding);
	}
}
