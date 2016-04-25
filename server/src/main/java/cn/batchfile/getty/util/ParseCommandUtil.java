package cn.batchfile.getty.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ParseCommandUtil {

	/**
	 * 获取命令
	 * 
	 * @param args
	 *            Main参数列表
	 * @return 命令值
	 */
	public static String getCommand(String[] args) {
		String command = StringUtils.EMPTY;
		for (String arg : args) {
			if (!StringUtils.startsWithIgnoreCase(arg, "-")) {
				command = arg;
				break;
			}
		}
		return command;
	}

	/**
	 * 获取参数（数组）
	 * 
	 * @param args
	 *            Main参数列表
	 * @param name
	 *            参数名称（全名称）
	 * @param shortName
	 *            参数短名称
	 * @return 参数值（数组）
	 */
	public static List<String> getArgsList(String[] args, String name,
			String shortName) {
		String fulltext = String.format("--%s=", name);
		String shorttext = String.format("-%s", shortName);

		List<String> list = new ArrayList<String>();

		for (String arg : args) {
			if (StringUtils.startsWithIgnoreCase(arg, fulltext)) {
				list.add(StringUtils.substring(arg, fulltext.length()));
			} else if (StringUtils.startsWithIgnoreCase(arg, shorttext)) {
				list.add(StringUtils.substring(arg, shorttext.length()));
			}
		}
		return list;
	}

	/**
	 * 获取参数
	 * 
	 * @param args
	 *            Main参数列表
	 * @param name
	 *            参数名称（全名称）
	 * @param shortName
	 *            参数短名称
	 * @return 参数值
	 */
	public static String getArgs(String[] args, String name, String shortName) {
		String fulltext = String.format("--%s=", name);
		String shorttext = String.format("-%s", shortName);

		for (String arg : args) {
			if (StringUtils.startsWithIgnoreCase(arg, fulltext)) {
				return StringUtils.substring(arg, fulltext.length());
			} else if (StringUtils.startsWithIgnoreCase(arg, shorttext)) {
				return StringUtils.substring(arg, shorttext.length());
			}
		}
		return null;
	}
}