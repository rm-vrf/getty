package cn.batchfile.getty.parser;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Parser {
	public static final String GROOVY_EXTENSION = ".groovy";

	public abstract void parse(File file, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
