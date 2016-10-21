package cn.batchfile.getty.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
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

import cn.batchfile.getty.util.ParseCommandUtil;
import cn.batchfile.getty.util.PlaceholderUtils;

public class Main {
	private static final Logger LOG = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		
		//解析参数
		String pidFile = ParseCommandUtil.getArgs(args, "pid-file", "p");
		String baseDir = ParseCommandUtil.getArgs(args, "base-dir", "b");
		String appsDir = ParseCommandUtil.getArgs(args, "apps-dir", "a");
		if (StringUtils.isEmpty(baseDir)) {
			baseDir = ".";
		}

		//写pid文件
		final Main main = new Main();
		if (StringUtils.isNotEmpty(pidFile)) {
			main.writePidFile(pidFile);
		}
		
		//加载日志
		main.configLog(baseDir, "conf/log4j.properties");
		LOG.info("  ____      _   _           ");
		LOG.info(" / ___| ___| |_| |_ _   _   ");
		LOG.info("| |  _ / _ \\ __| __| | | |  ");
		LOG.info("| |_| |  __/ |_| |_| |_| |  ");
		LOG.info(" \\____|\\___|\\__|\\__|\\__, |  ");
		LOG.info("                    |___/   ");
		LOG.info("Groovy on Jetty!            ");
		
		//设置服务参数
		final Getty getty = new Getty();
		getty.setApplicationsDirectory(appsDir);
		getty.setBaseDirectory(baseDir);
		
		//启动服务
		getty.start();
		
		//线程钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LOG.info("stop getty");
				getty.stop();
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
			public void onStart(FileAlterationObserver observer) {
			}
			
			@Override
			public void onStop(FileAlterationObserver observer) {
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
			public void onFileDelete(File file) {
				LOG.info(propertiesFile.getName() +  " deleted");
			}
			
			@Override
			public void onDirectoryCreate(File file) {
			}
			
			@Override
			public void onDirectoryChange(File file) {
			}
			
			@Override
			public void onDirectoryDelete(File file) {
			}
		});
		FileAlterationMonitor monitor = new FileAlterationMonitor(2000, observer);
		monitor.start();
	}
	
	private void configLog(File file, Map<String, String> vars) {
		if (file.exists()) {
			LOG.info("load log4j.properties from " + file.getAbsolutePath());
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
			LOG.info("cannot find log4j.properties in " + file.getAbsolutePath());
		}
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
