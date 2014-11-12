package cn.batchfile.getty.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import cn.batchfile.getty.configuration.Configuration;

public class DirectoryParser extends Parser {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private Configuration configuration;
	
	public DirectoryParser(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void parse(File dir, HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html");
		response.setCharacterEncoding(configuration.charset());
		
		head(dir, request, response);
		tableHead(dir, request, response);
		File readme = tableBody(dir, request, response);
		tableTail(dir, request, response);
		if (readme != null) {
			readme(readme, request, response);
		}
		tail(dir, request, response);
	}
	
	private void head(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = request.getRequestURI();
		
		response.getWriter().println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">");
		response.getWriter().println("<html>");
		response.getWriter().println(" <head>");
		response.getWriter().println("  <title>Index of " + title + "</title>");
		response.getWriter().println(" </head>");
		response.getWriter().println(" <body>");
		response.getWriter().println("<h1>Index of " + title + "</h1>");
	}
	
	private void tableHead(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = request.getRequestURI();
		String parentDir = StringUtils.substringBeforeLast(title, "/");
		if (StringUtils.isEmpty(parentDir)) {
			parentDir = "/";
		}
		
		response.getWriter().println("  <table>");
		response.getWriter().println("   <tr><th valign=\"top\"><img src=\"/_cp/images/blank.png\" alt=\"[ICO]\"></th><th>Name</th><th>Last modified</th><th>Size</th><th>Description</th></tr>");
		response.getWriter().println("   <tr><th colspan=\"5\"><hr></th></tr>");
		response.getWriter().println("<tr><td valign=\"top\"><img src=\"/_cp/images/back.png\" alt=\"[PARENTDIR]\"></td><td><a href=\"" + parentDir + "\">Parent Directory</a></td><td>&nbsp;</td><td align=\"right\">  - </td><td>&nbsp;</td></tr>");
	}
	
	private File tableBody(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		File readme = null;
		List<File> files = Arrays.asList(dir.listFiles());
		Collections.sort(files, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				String name1 = getComparatorName(file1);
				String name2 = getComparatorName(file2);
				return name1.compareTo(name2);
			}
		});
		
		for (File file : files) {
			String name = file.getName();
			if (StringUtils.equalsIgnoreCase("readme.html", name) ||
					StringUtils.equals("readme.html", name)) {
				readme = file;
			}
			String href = request.getRequestURI();
			if (!StringUtils.endsWith(href, "/")) {
				href += "/";
			}
			href += name;
			String time = format.format(file.lastModified());
			String type = file.isDirectory() ? "DIR" : "FILE";
			String icon = file.isDirectory() ? "folder" : "generic";
			String size = getSize(file);
			response.getWriter().println("<tr><td valign=\"top\"><img src=\"/_cp/images/" + icon + ".png\" alt=\"[" + type + "]\"></td><td><a href=\"" + href + "\">" + name + "</a></td><td align=\"right\"> &nbsp; " + time + "</td><td align=\"right\"> &nbsp; " + size + " </td><td>&nbsp;</td></tr>");
		}
		
		return readme;
	}
	
	private void tableTail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("   <tr><th colspan=\"5\"><hr></th></tr>");
		response.getWriter().println("</table>");
	}
	
	private void readme(File readme, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<String> lines = IOUtils.readLines(new FileInputStream(readme));
		for (String line : lines) {
			response.getWriter().println(line);
		}
	}

	private void tail(File dir, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("</body></html>");
	}

	private String getSize(File file) {
		if (file.isDirectory()) {
			return "-";
		}
		float length = file.length();
		if (length == 0) {
			return "0";
		} else if (length < 1024) {
			return String.format("%.0f", (float)length);
		} else if (length < 1024 * 1024) {
			return String.format("%.1fK", (float)(length / 1024F));
		} else if (length < 1024 * 1024 * 1024) {
			return String.format("%.1fM", (float)(length / 1024F / 1024F));
		} else {
			return String.format("%.1fG", (float)(length / 1024F / 1024F / 1024F));
		}
	}
	
	private String getComparatorName(File file) {
		return String.format("%s:%s", file.isDirectory() ? "D" : "F", file.getName().toLowerCase());
	}
	
}
