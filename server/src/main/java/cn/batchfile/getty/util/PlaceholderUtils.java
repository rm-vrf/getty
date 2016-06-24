package cn.batchfile.getty.util;

import java.util.Map;

import org.apache.log4j.Logger;

public class PlaceholderUtils {

	private static final Logger logger = Logger.getLogger(PlaceholderUtils.class);

	/**
	 * Prefix for system property placeholders: "${"
	 */
	public static final String PLACEHOLDER_PREFIX = "${";
	
	/**
	 * Suffix for system property placeholders: "}"
	 */
	public static final String PLACEHOLDER_SUFFIX = "}";

	public static String resolvePlaceholders(String text, Map<String, String> parameter) {
		if (parameter == null || parameter.isEmpty()) {
			return text;
		}
		
		StringBuffer buf = new StringBuffer(text);
		int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
		
		while (startIndex != -1) {
			int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
				int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
				try {
					String propVal = parameter.get(placeholder);
					if (propVal != null) {
						buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
						nextIndex = startIndex + propVal.length();
					} else {
						logger.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
					}
				} catch (Exception ex) {
					logger.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
				}
				startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
			} else {
				startIndex = -1;
			}
		}
		return buf.toString();
	}

}