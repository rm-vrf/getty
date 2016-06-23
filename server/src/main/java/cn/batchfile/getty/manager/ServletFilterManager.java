package cn.batchfile.getty.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import cn.batchfile.getty.application.Application;
import cn.batchfile.getty.application.ApplicationInstance;
import cn.batchfile.getty.binding.http.Cookie;
import cn.batchfile.getty.binding.http.Request;
import cn.batchfile.getty.binding.http.Response;
import cn.batchfile.getty.binding.http.Session;

public class ServletFilterManager implements Filter {
	
	private static final Logger LOG = Logger.getLogger(ServletFilterManager.class);
	private Application application;
	private ApplicationInstance applicationInstance;
	private ScriptEngineManager scriptEngineManager;

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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//pass
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpServletResponse servletResponse = (HttpServletResponse)response;

		//得到访问地址
		String uri = servletRequest.getRequestURI();
		
		if (application.getFilters() != null) {
			//判断每一个过滤器配置
			for (cn.batchfile.getty.application.Filter filter : application.getFilters()) {
				
				//命中过滤模式，执行过滤器脚本
				if (FilenameUtils.wildcardMatch(uri, filter.getUrlPattern())) {
					execute(filter.getScript(), servletRequest, servletResponse);
				}
			}
		}

		//继续
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		//pass
	}
	
	private void execute(String file, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
		
		Request bindingRequest = new Request(servletRequest, applicationInstance);
		Response bindingResponse = new Response(servletRequest, servletResponse);
		Session bindingSession = new Session(servletRequest);
		Cookie bindingCookie = new Cookie(servletRequest, servletResponse);
		Logger bindingLogger = Logger.getLogger(file);
		
		//binding inner object
		Map<String, Object> binding = new HashMap<String, Object>();
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);
		binding.put("$application", applicationInstance);
		binding.put("$app", applicationInstance);
		binding.put("$request", bindingRequest);
		binding.put("$req", bindingRequest);
		binding.put("$response", bindingResponse);
		binding.put("$res", bindingResponse);
		binding.put("$resp", bindingResponse);
		binding.put("$session", bindingSession);
		binding.put("$cookie", bindingCookie);
		binding.put("$logger", bindingLogger);
		binding.put("$log", bindingLogger);

		Object r = scriptEngineManager.run(file, binding);
		if (LOG.isDebugEnabled()) {
			LOG.debug("shell return value: " + r);
		}
	}
	
}
