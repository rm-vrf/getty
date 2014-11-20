package cn.batchfile.getty.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import cn.batchfile.getty.exceptions.RewriteMappingException;

public class Rewriter {
	public static final String CONFIG_FILE = "_rewrite.json";
	
	private static final Logger LOG = Logger.getLogger(Rewriter.class);
	private List<Pattern> patterns = new ArrayList<Pattern>();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void config(File directory) throws JsonParseException, JsonMappingException, IOException {
		patterns.clear();
		configDir(directory);
	}
	
	private void configDir(File directory) throws JsonParseException, JsonMappingException, IOException {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				configDir(file);
			} else if (file.getName().equals(CONFIG_FILE)) {
				LOG.info(String.format("Load rewrite config: %s", file.getPath()));
				configFile(file);
			}
		}
	}
	
	public String mapping(HttpServletRequest request, Map<String, Object> vars) throws RewriteMappingException {
		
		//检查uri模式
		Pattern pattern = getPatternByUri(request.getRequestURI(), vars);
		if (pattern == null) {
			return null;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("match uri pattern: " + pattern.getPattern() + " with path vars: " + vars.toString());
		}
		
		//检查method
		if (!matchMethod(pattern.getMethods(), request.getMethod())) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("method mismatch");
			}
			String message = String.format("method %s not support for uri %s", request.getMethod(), pattern.getUri());
			throw new RewriteMappingException(message);
		}
		
		//检查消息头
		if (!matchHeader(pattern.getHeaders(), getHeaders(request))) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("header mismatch");
			}
			String message = String.format("header not support for uri %s", pattern.getUri());
			throw new RewriteMappingException(message);
		}
		
		return replacePlaceHolder(pattern.getUri(), vars);
	}
	
	private String replacePlaceHolder(String s, Map<String, Object> vars) {
		while (StringUtils.contains(s, '{') && StringUtils.contains(s, '}')) {
			int begin = StringUtils.indexOf(s, '{');
			int end = StringUtils.indexOf(s, '}', begin);
			String key = StringUtils.substring(s, begin + 1, end);
			Object value = vars.get(key);
			s = StringUtils.substring(s, 0, begin) + value + StringUtils.substring(s, end + 1);
		}
		return s;
	}
	
	private boolean matchHeader(Map<String, String> patternHeaders, Map<String, String> requestHeaders) {
		for (Entry<String, String> entry : patternHeaders.entrySet()) {
			String name = entry.getKey();
			String patternValue = entry.getValue();
			String requestValue = requestHeaders.get(name);
			
			if (!StringUtils.equalsIgnoreCase(patternValue, requestValue)) {
				return false;
			}
		}
		return true;
	}

	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			map.put(name, request.getHeader(name));
		}
		return map;
	}
	
	private boolean matchMethod(List<String> patternMethods, String uriMethod) {
		if (CollectionUtils.isEmpty(patternMethods)) {
			return true;
		} else if (patternMethods.contains(uriMethod.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	private Pattern getPatternByUri(String uri, Map<String, Object> vars) {
		List<String> requestParts = getParts(uri);
		for (Pattern pattern : patterns) {
			vars.clear();
			if (matchUri(pattern.getParts(), requestParts, vars)) {
				return pattern;
			}
		}
		return null;
	}
	
	private boolean matchUri(List<String> patternParts, List<String> uriParts, Map<String, Object> vars) {
		if (patternParts.size() != uriParts.size()) {
			return false;
		}
		
		for (int i = 0; i < patternParts.size(); i ++) {
			String patternPart = patternParts.get(i);
			String uriPart = uriParts.get(i);
			if (StringUtils.startsWith(patternPart, "{") && StringUtils.endsWith(patternPart, "}")) {
				vars.put(StringUtils.substringBetween(patternPart, "{", "}"), uriPart);
			} else if (!StringUtils.equals(patternPart, uriPart)) {
				return false;
			}
		}
		
		return true;
	}
	
	private void configFile(File file) throws JsonParseException, JsonMappingException, IOException {
		if (file.length() == 0) {
			LOG.info("empty config file");
			return;
		}
		
		List<Map<String, Object>> list = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
		for (Map<String, Object> map : list) {
			Pattern p = new Pattern();
			patterns.add(p);

			//加载uri和对应的模式
			p.setUri(map.get("uri").toString());
			p.setPattern(map.get("pattern").toString());

			//加载方法
			Object methods = map.get("method");
			if (methods instanceof List) {
				@SuppressWarnings("unchecked")
				List<String> l = (List<String>)methods;
				for (String s : l) {
					p.getMethods().add(s.toUpperCase());
				}
			} else {
				p.getMethods().add(methods.toString().toUpperCase());
			}
			
			//加载消息头
			Object headers = map.get("header");
			if (headers instanceof List) {
				@SuppressWarnings("unchecked")
				List<String> l = (List<String>)headers;
				for (String s : l) {
					String[] ary = StringUtils.split(s, ':');
					if (ary != null && ary.length >=2) {
						p.getHeaders().put(ary[0].trim(), ary[1].trim());
					}
				}
			} else if (headers != null) {
				String s = headers.toString();
				String[] ary = StringUtils.split(s, ':');
				if (ary != null && ary.length >=2) {
					p.getHeaders().put(ary[0].trim(), ary[1].trim());
				}
			}
			
			//分解模式的部分
			p.setParts(getParts(p.getPattern()));
		}
	}
	
	private List<String> getParts(String uri) {
		String[] ary = StringUtils.split(uri, '/');
		List<String> l = new ArrayList<String>();
		for (String s : ary) {
			if (StringUtils.isNotEmpty(s)) {
				l.add(s);
			}
		}
		return l;
	}
}
