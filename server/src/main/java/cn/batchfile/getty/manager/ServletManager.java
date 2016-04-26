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

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.Handler;
import cn.batchfile.getty.binding.Cookie;
import cn.batchfile.getty.binding.Model;
import cn.batchfile.getty.binding.Request;
import cn.batchfile.getty.binding.Response;
import cn.batchfile.getty.binding.Session;
import cn.batchfile.getty.binding.View;
import cn.batchfile.getty.util.MimeTypes;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ServletManager implements Servlet {
	
	private static final Logger logger = Logger.getLogger(ServletManager.class);
	private MappingManager mappingManager;
	private Application application;
	private MimeTypes mimeTypes = new MimeTypes();

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
		response.getWriter().println(String.format("<HTML><HEAD><TITLE>Directory: %s</TITLE></HEAD><BODY>", title));
		response.getWriter().println(String.format("<H1>Directory: %s</H1>", title));
	}

	private void printTableHead(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("<TABLE BORDER=0>");
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
			response.getWriter().println(String.format("<TR><TD><A HREF=\"%s\">%s&nbsp;</A></TD><TD ALIGN=right>%s bytes&nbsp;</TD><TD>%s</TD></TR>", uri, name, length, time));
		}
	}
	
	private void printTableTail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("</TABLE>");
	}
	
	private void printTail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("</BODY></HTML>");
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
	
	private void outputGroovy(File file, HttpServletRequest request, HttpServletResponse response, Map<String, Object> vars) throws IOException {
		Request bindingRequest = new Request(request, null);
		Response bindingResponse = new Response(response);
		Session bindingSession = new Session(request);
		Cookie bindingCookie = new Cookie(request, response);
		Model bindingModel = new Model(request);
		View bindingView = new View(request, response);
		Logger bindingLogger = Logger.getLogger(file.getName());
		
		//binding inner object
		Binding binding = new Binding();
		binding.setProperty("$application", application);
		binding.setProperty("$app", application);
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
		String text = FileUtils.readFileToString(file, application.getFileEncoding());
		GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding);
		Object r = shell.evaluate(text, file.getName());
		if (logger.isDebugEnabled()) {
			logger.debug("shell return value: " + r);
		}
		
		//process content-type
		if (StringUtils.isEmpty(response.getContentType())) {
			response.setContentType("text/html");
		}
	}

}
