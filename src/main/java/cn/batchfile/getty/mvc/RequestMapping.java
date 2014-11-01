package cn.batchfile.getty.mvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.exceptions.ListDirectoryNotAllowedException;

public class RequestMapping {
	public static final String CLASSPATH_PREFIX = "/-cp";
	private static final Logger logger = Logger.getLogger(RequestMapping.class);
	private Map<String, File> classpathFiles = new ConcurrentHashMap<String, File>();
	private Configuration configuration;
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public File mapping(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String context = configuration.getContextPath();
		uri = StringUtils.substring(uri, context.length());
		
		if (StringUtils.startsWith(uri, CLASSPATH_PREFIX)) {
			return findClasspathResource(StringUtils.substring(uri, CLASSPATH_PREFIX.length()));
		}
		
		String path = configuration.getBaseDirectory() + configuration.getWebRoot() + uri;
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("mapping uri: %s to: %s", uri, path));
		}
		
		File file = new File(path);
		
		//如果是目录，寻找目录中的默认文档
		if (file.isDirectory()) {
			if (!configuration.isAllowListDirectory()) {
				throw new ListDirectoryNotAllowedException();
			}
			
			for (String indexPage : configuration.getIndexPages()) {
				String page = file.getAbsolutePath() + "/" + indexPage;
				File f = new File(page);
				if (f.exists()) {
					return f;
				}
			}
			if (configuration.isAllowListDirectory()) {
				return file;
			} else {
				throw new ListDirectoryNotAllowedException();
			}
		} else {
			return file;
		}
	}
	
	private File findClasspathResource(String uri) {
		if (StringUtils.startsWith(uri, "/")) {
			uri = StringUtils.substring(uri, 1);
		}
		
		if (!classpathFiles.containsKey(uri)) {
			initClasspathFile(uri);
		}
		
		return classpathFiles.get(uri);
	}
	
	private String getExtension(String file) {
		String name = file;
		if (StringUtils.contains(name, '/')) {
			name = StringUtils.substringAfterLast(name, "/");
		}
		return StringUtils.substringAfterLast(name, ".");
	}
	
	synchronized private void initClasspathFile(String uri) {
		String ext = getExtension(uri);
		String tmpFile = System.getProperty("java.io.tmpdir", "/tmp");
		tmpFile += "/" + UUID.randomUUID().toString() + "." + ext;
		
		File file = new File(tmpFile);
		InputStream stream = null;
		OutputStream output = null;
		try {
			stream = getClass().getClassLoader().getResourceAsStream(uri);
			output = new FileOutputStream(file);
			IOUtils.copy(stream, output);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(output);
		}
		classpathFiles.put(uri, file);
	}
}
