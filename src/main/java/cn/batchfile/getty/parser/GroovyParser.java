package cn.batchfile.getty.parser;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.batchfile.getty.binding.Request;
import cn.batchfile.getty.binding.Response;

public class GroovyParser extends Parser {

	@Override
	public void parse(File file, HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
		//binding object
		Request _request = new Request(request);
		Response _response = new Response(response);
		
		Binding binding = new Binding();
		binding.setProperty("_request", _request);
		binding.setProperty("_response", _response);
		
		//binding input param
		for (Entry<String, Object> entry : _request.parameters().entrySet()) {
			binding.setVariable(entry.getKey(), entry.getValue());
		}

		//execute script file
		GroovyShell shell = new GroovyShell(binding);
		shell.evaluate(file);
	}
}
