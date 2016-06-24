package cn.batchfile.getty.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.manager.ApplicationInstanceManager;
import cn.batchfile.getty.manager.ApplicationManager;
import cn.batchfile.getty.util.ParseCommandUtil;
import cn.batchfile.getty.util.PlaceholderUtils;

public class Main {
	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		
		//解析参数
		String baseDir = ParseCommandUtil.getArgs(args, "base-dir", "b");
		if (StringUtils.isEmpty(baseDir)) {
			baseDir = ".";
		}
		String appsDir = ParseCommandUtil.getArgs(args, "apps-dir", "a");
		String pidFile = ParseCommandUtil.getArgs(args, "pid-file", "p");

		//写pid文件
		Main main = new Main();
		if (StringUtils.isNotEmpty(pidFile)) {
			main.writePidFile(pidFile);
		}
		
		//加载日志
		main.configLog(baseDir, "conf/log.properties");
		LOG.info("-----start getty-----");
		
		//寻找应用目录
		List<File> appDirs = main.findAppDirs(baseDir, appsDir);
		
		//加载应用目录
		final ApplicationManager am = new ApplicationManager();
		final List<Application> applications = new ArrayList<Application>();
		for (File appDir : appDirs) {
			applications.add(am.load(appDir));
		}
		
		//启动应用
		final ApplicationInstanceManager aim = new ApplicationInstanceManager();
		for (Application application : applications) {
			aim.start(application);
		}
		
		//线程钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LOG.info("stop getty");
				for (Application application : applications) {
					aim.stop(application.getName());
					am.unload(application.getName());
				}
			}
		});
	}
	
	private void configLog(String base, String config) throws Exception {
		final Map<String, String> vars = new HashMap<String, String>();
		vars.put("base.dir", base);
		vars.put("baseDir", base);
		
		final File propertiesFile = new File(base, config);
		configLog(propertiesFile, vars);

		//创建log文件监听器
		FileAlterationObserver observer = new FileAlterationObserver(propertiesFile.getParent(), new NameFileFilter(propertiesFile.getName()), null);
		observer.addListener(new FileAlterationListener() {
			
			@Override
			public void onStop(FileAlterationObserver observer) {
			}
			
			@Override
			public void onStart(FileAlterationObserver observer) {
			}
			
			@Override
			public void onFileDelete(File file) {
				LOG.info(propertiesFile.getName() +  " deleted");
			}
			
			@Override
			public void onFileCreate(File file) {
				LOG.info(propertiesFile.getName() +  " created");
				configLog(file, vars);
			}
			
			@Override
			public void onFileChange(File file) {
				LOG.info(propertiesFile.getName() +  " changed");
				configLog(file, vars);
			}
			
			@Override
			public void onDirectoryDelete(File file) {
				LOG.info("delete directory in log config dir");
			}
			
			@Override
			public void onDirectoryCreate(File file) {
				LOG.info("create directory in log config dir");
			}
			
			@Override
			public void onDirectoryChange(File file) {
				LOG.info("change directory in log config dir");
			}
		});
		FileAlterationMonitor monitor = new FileAlterationMonitor(2000, observer);
		monitor.start();
	}
	
	private void configLog(File file, Map<String, String> vars) {
		if (file.exists()) {
			LOG.info("load log.properties from " + file.getAbsolutePath());
			try {
				String content = FileUtils.readFileToString(file);
				content = PlaceholderUtils.resolvePlaceholders(content, vars);
				
				InputStream stream = new ByteArrayInputStream(content.getBytes());
				Properties props = new Properties();
				props.load(stream);
				BasicConfigurator.resetConfiguration();
				PropertyConfigurator.configure(props);
			} catch (IOException e) {
				throw new RuntimeException("error when load log config", e);
			}
		} else {
			LOG.info("cannot find log.properties in " + file.getAbsolutePath());
		}
	}

	private List<File> findAppDirs(String baseDir, String appsDir) {
		List<File> list = new ArrayList<File>();
		File webapps = null;
		if (StringUtils.isEmpty(appsDir)) {
			//命令行没有给apps-dir参数，寻找默认的位置
			webapps = new File(baseDir, "webapps");
		} else {
			//命令行给了apps-dir参数，按照参数寻找 
			webapps = new File(appsDir);
		}
		
		//找到webapps路径, 遍历子目录
		if (webapps.exists()) {
			LOG.info("application root: " + webapps);
			File[] dirs = webapps.listFiles();
			for (File dir : dirs) {
				//目录里面有项目文件，加载这个目录
				if (dir.isDirectory() && new File(dir, "app.yaml").exists()) {
					list.add(dir);
				}
			}
		} 
		
		//不存在webapps目录，按照调试模式寻找
		if (!webapps.exists()) {
			String[] dirs = new String[] {"webapp", "src/main/webapp"};
			for (String dir : dirs) {
				File app = new File(baseDir, dir);
				if (app.exists() && app.isDirectory() 
						&& new File(app, "app.yaml").exists()) {
					list.add(app);
					LOG.info("application root: " + app);
					break;
				}
			}
		}
		
		return list;
	}

	private void writePidFile(String pidFile) throws IOException {
		if (!StringUtils.isBlank(pidFile)) {
			String pid = StringUtils.substringBefore(ManagementFactory.getRuntimeMXBean().getName(), "@");
			File file = new File(pidFile);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				if (!dir.exists()) {
					FileUtils.forceMkdir(dir);
				}
				file.createNewFile();
			}
			FileUtils.writeStringToFile(file, pid);
			LOG.info(String.format("pid file: %s, pid: %s", pidFile, pid));
		}
	}
}
