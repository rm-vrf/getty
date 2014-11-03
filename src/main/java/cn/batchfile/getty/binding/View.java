package cn.batchfile.getty.binding;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class View {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public View(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public void redirect(String location) throws IOException {
		response.sendRedirect(location);
	}

	public void jsp(String view) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}
