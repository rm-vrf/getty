package cn.batchfile.getty.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import cn.batchfile.getty.configuration.Configuration;
import cn.batchfile.getty.util.MimeTypes;

public class StaticParser extends Parser {

	private Configuration configuration;
	private MimeTypes mimeTypes = new MimeTypes();
	
	public StaticParser(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void parse(File file, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> vars) throws IOException {
		
		response.setContentType(mimeTypes.getMimeByExtension(file.getName()));
		response.setCharacterEncoding(configuration.charset());
		
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			response.sendError(404, String.format("File not found: %s", file));
		}
		
		try {
			IOUtils.copy(stream, response.getOutputStream());
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
