package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
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
		super.init(filterConfig);
		executor.init();
	}
	
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		boolean served = executor.service(request, response);
		if (!served){
			delegateToDefaultServlet(request, response);
		}
	}
	
	private void delegateToDefaultServlet(ServletRequest request, ServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
		rd.forward(request, response);
	}

	@Override
	public void destroy() {
		executor.destroy();
	}

}
