package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

@Singleton
public class FunrouteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2778983266646306877L;

	@Inject
	private HttpRequestExecutor executor;

	@Override
	public void init(ServletConfig filterConfig) throws ServletException {
		executor.init();
	}
	
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		executor.service(request, response);
	}

	@Override
	public void destroy() {
		executor.destroy();
	}

}
