package cn.batchfile.getty.binding;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

	private Map<String, Object> map = new ConcurrentHashMap<String, Object>();
	private static Application instance = new Application();
	
	public static Application getInstance() {
		return instance;
	}
	
	private Application() {
		
	}
	
	public Object get(String name) {
		return map.get(name);
	}
	
	public Map<String, Object> get() {
		return map;
	}
	
	public void put(String name, String value) {
		map.put(name, value);
	}
	
	public void remove(String name) {
		map.remove(name);
	}
	
	public void removeAll() {
		map.clear();
	}
}
