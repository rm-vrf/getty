package cn.batchfile.getty.lang;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class ApplicationClassLoader extends URLClassLoader {

	private static final Logger logger = Logger.getLogger(ApplicationClassLoader.class);
	private ClassLoader parent;
	
	public ApplicationClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		this.parent = parent;
		logger.debug("init ApplicationClassLoader");
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
        		logger.debug("loadClass(" + name + ")");
            	try {
            		//Invoke findClass in order to find the class.
            		c = findClass(name);
            	} catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from this class loader
            	}
            	
            	if (c == null) {
                    // If still not found, then search non-null parent class loader 
            		// in order to find the class.
                    if (parent != null) {
                        c = parent.loadClass(name);
                    }
            	}
            }
            return c;
        }
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		logger.debug("getResources(" + name + ")");
		Enumeration<URL> urls = super.getResources(name);
		return urls;
	}
	
	@Override
	public URL getResource(String name) {
		logger.debug("getResource(" + name + ")");
        URL url = findResource(name);
        if (url == null) {
        	url = super.getResource(name);
        }        
        return url;
	}
}
