package cn.batchfile.getty.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.application.Handler;
import cn.batchfile.getty.binding.Cookie;
import cn.batchfile.getty.binding.Model;
import cn.batchfile.getty.binding.Request;
import cn.batchfile.getty.binding.Response;
import cn.batchfile.getty.binding.Session;
import cn.batchfile.getty.binding.View;
import cn.batchfile.getty.util.MimeTypes;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class ServletManager implements Servlet {
	
	private static final Logger logger = Logger.getLogger(ServletManager.class);
	private static final String[] FORBIDDEN_PAGES = new String[] {
			"/classes", 
			"/lib", 
			"/app.yaml", 
			"/cron.yaml", 
			"/log.yaml", 
			"/session.yaml", 
			"/ws.yaml"};
	
	private MappingManager mappingManager;
	private Application application;
	private ApplicationInstance applicationInstance;
	private MimeTypes mimeTypes = new MimeTypes();
	private ClassLoader classLoader;
	private Map<String, GroovyScriptEngine> gses = new ConcurrentHashMap<String, GroovyScriptEngine>();

	public MappingManager getMappingManager() {
		return mappingManager;
	}

	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}

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

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void destroy() {
		// pass
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// pass
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		String uri = request.getRequestURI();
		
		//get mapping file
		Map<String, Object> vars = new HashMap<String, Object>();
		Handler handler = mappingManager.mapping(uri, vars);
		if (handler != null) {
			//从url映射里寻找文件
			uri = replaceVars(handler.getScript(), vars);
		}
		
		//排除系统文件
		if (isForbidden(uri)) {
			response.sendError(403, "Forbidden");
			return;
		}
		
		//映射文件
		File file = new File(application.getDir(), uri);
		
		//如果映射文件是个目录，尝试默认文档
		if (file.exists() && file.isDirectory()) {
			File defaultPage = getDefaultPage(file, application.getIndexPages());
			if (defaultPage != null) {
				file = defaultPage;
			}
		}
			
		//如果映射文件不存在，抛出异常
		if (!file.exists()) {
			response.sendError(404, "Not Found");
			return;
		}
		
		//找到映射文件，输出这个文件
		output(file, request, response, vars);
	}
	
	private boolean isForbidden(String uri) {
		for (String page : FORBIDDEN_PAGES) {
			if (StringUtils.startsWith(uri, page)) {
				return true;
			}
		}
		return false;
	}

	private File getDefaultPage(File directory, List<String> defaultPages) {
		for (String defaultPage : defaultPages) {
			File file = new File(directory, defaultPage);
			if (file.exists()) {
				return file;
			}
		}
		
		return null;
	}

	private String replaceVars(String s, Map<String, Object> vars) {
		while (StringUtils.contains(s, '(') && StringUtils.contains(s, ')')) {
			int begin = StringUtils.indexOf(s, '(');
			int end = StringUtils.indexOf(s, ')', begin);
			String key = StringUtils.substring(s, begin + 1, end);
			Object value = vars.get(key);
			s = StringUtils.substring(s, 0, begin) + value + StringUtils.substring(s, end + 1);
		}
		return s;
	}
	
	private boolean isGroovy(File file) {
		return StringUtils.endsWithIgnoreCase(file.getName(), ".groovy");
	}
	
	private void output(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) throws IOException {
		if (file.isDirectory()) {
			outputDirectory(file, request, response, vars);
		} else if (isGroovy(file)) {
			outputGroovy(file, request, response, vars);
		} else {
			outputStatic(file, request, response, vars);
		}
	}
	
	private void outputDirectory(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding(application.getCharsetEncoding());
		
		printHead(file, request, response);
		printTableHead(file, request, response);
		printTableBody(file, request, response);
		printTableTail(file, request, response);
		printTail(file, request, response);
	}
	
	private void printHead(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = request.getRequestURI();
		response.getWriter().println(String.format("<html><head><title>Directory: %s</title></head><body>", title));
		response.getWriter().println(String.format("<h1>Directory: %s</h1>", title));
	}

	private void printTableHead(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("<table border=\"0\">");
	}

	private void printTableBody(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		File[] files = dir.listFiles();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d H:m:s");
		for (File file : files) {
			String uri = request.getRequestURI();
			if (StringUtils.endsWith(uri, "/")) {
				uri += file.getName();
			} else {
				uri += "/" + file.getName();
			}
			String name = file.getName() + (file.isDirectory() ? "/" : StringUtils.EMPTY);
			long length = file.length();
			String time = df.format(new Date(file.lastModified()));
			response.getWriter().println(String.format("<tr><td><a href=\"%s\">%s&nbsp;</a></td><td align=\"right\">%s bytes&nbsp;</td><td>%s</td></tr>", uri, name, length, time));
		}
	}
	
	private void printTableTail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("</table>");
	}
	
	private void printTail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("</body></html>");
	}
	
	private void outputStatic(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) throws IOException {
		response.setContentType(mimeTypes.getMimeByExtension(file.getName()));
		response.setCharacterEncoding(application.getCharsetEncoding());
		
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			response.sendError(404, String.format("Not Found: %s", file));
			return;
		}

		try {
			IOUtils.copy(stream, response.getOutputStream());
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	private void outputGroovy(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) {
		Request bindingRequest = new Request(request, null);
		Response bindingResponse = new Response(response);
		Session bindingSession = new Session(request);
		Cookie bindingCookie = new Cookie(request, response);
		Model bindingModel = new Model(request);
		View bindingView = new View(request, response);
		Logger bindingLogger = Logger.getLogger(file.getName());
		
		//binding inner object
		Binding binding = new Binding();
		binding.setProperty("$application", applicationInstance);
		binding.setProperty("$app", applicationInstance);
		binding.setProperty("$request", bindingRequest);
		binding.setProperty("$req", bindingRequest);
		binding.setProperty("$response", bindingResponse);
		binding.setProperty("$res", bindingResponse);
		binding.setProperty("$resp", bindingResponse);
		binding.setProperty("$session", bindingSession);
		binding.setProperty("$cookie", bindingCookie);
		binding.setProperty("$model", bindingModel);
		binding.setProperty("$mod", bindingModel);
		binding.setProperty("$m", bindingModel);
		binding.setProperty("$view", bindingView);
		binding.setProperty("$v", bindingView);
		binding.setProperty("$logger", bindingLogger);
		binding.setProperty("$log", bindingLogger);
		binding.setProperty("$vars", vars);

		//binding path vars
		for (Entry<String, Object> entry : vars.entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}

		//binding input param
		for (Entry<String, Object> entry : bindingRequest.getParameters().entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}

		//set response charset
		response.setCharacterEncoding(application.getCharsetEncoding());
		
		//execute script file
		try {
			GroovyScriptEngine gse = getGroovyScriptEngine(file);
			Object r = gse.run(file.getName(), binding);
			if (logger.isDebugEnabled()) {
				logger.debug("shell return value: " + r);
			}
		} catch (IOException e) {
			throw new RuntimeException("script exception: " + file.getName(), e);
		} catch (ResourceException e) {
			throw new RuntimeException("script exception: " + file.getName(), e);
		} catch (ScriptException e) {
			throw new RuntimeException("script exception: " + file.getName(), e);
		}
		
		//process content-type
		if (StringUtils.isEmpty(response.getContentType())) {
			response.setContentType("text/html");
		}
	}
	
	private GroovyScriptEngine getGroovyScriptEngine(File file) throws IOException {
		String key = file.getAbsolutePath();
		if (!gses.containsKey(key)) {
			synchronized (gses) {
				if (!gses.containsKey(key)) {
					String path = file.getParentFile().getAbsolutePath();
					GroovyScriptEngine gse = new GroovyScriptEngine(path, classLoader);
					gses.put(key, gse);
				}
			}
		}
		return gses.get(key);
	}

}
