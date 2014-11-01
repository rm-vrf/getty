package cn.batchfile.getty.mvc;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.exceptions.ListDirectoryNotAllowedException;

public class RequestMapping {
	private static final Logger logger = Logger.getLogger(RequestMapping.class);
	private Configuration configuration;
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public File mapping(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String context = configuration.getContextPath();
		uri = StringUtils.substring(uri, context.length());
		
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
			throw new ListDirectoryNotAllowedException();
		} else {
			return file;
		}
	}
}
