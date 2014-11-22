package cn.batchfile.getty.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import cn.batchfile.getty.configuration.Configuration;

public class Log4jConfigurator {

	public void load(String baseDirectory, Configuration configuration) throws UnsupportedEncodingException, IOException {
		String config = getLog4jConfig();
		String[] search = new String[] {
				"{LOG_LEVEL}", 
				"{APP_LOG}"
				};
		String[] replacement = new String[] {
				configuration.getLogLevel(), 
				baseDirectory + File.separatorChar + "log" + File.separatorChar + "app.log"
				};
		config = StringUtils.replaceEach(config, search, replacement);
		
		InputStream stream = IOUtils.toInputStream(config, "UTF-8");
		Properties props = new Properties();
		props.load(stream);
		BasicConfigurator.resetConfiguration();
		PropertyConfigurator.configure(props);
	}
	
	private String getLog4jConfig() throws UnsupportedEncodingException, IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("log4j");
		String s = new String(IOUtils.toByteArray(stream), "UTF-8");
		return s;
	}
}
