package cn.batchfile.getty.server;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.manager.ApplicationInstanceManager;
import cn.batchfile.getty.manager.ApplicationManager;
import cn.batchfile.getty.util.ParseCommandUtil;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		logger.info("-----start getty-----");
		Main main = new Main();
		
		//解析参数
		String baseDir = ParseCommandUtil.getArgs(args, "base-dir", "b");
		String appsDir = ParseCommandUtil.getArgs(args, "apps-dir", "a");
		String pidFile = ParseCommandUtil.getArgs(args, "pid-file", "p");

		//写pid文件
		if (StringUtils.isNotEmpty(pidFile)) {
			main.writePidFile(pidFile);
		}
		
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
				logger.info("stop getty");
				for (Application application : applications) {
					aim.stop(application.getName());
					am.unload(application.getName());
				}
			}
		});
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
			logger.info("application root: " + webapps);
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
					logger.info("application root: " + app);
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
			logger.info(String.format("pid-file: %s, pid: %s", pidFile, pid));
		}
	}
}
