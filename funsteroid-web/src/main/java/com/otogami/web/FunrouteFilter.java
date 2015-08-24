package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Singleton
public class FunrouteFilter implements Filter {
	
	@Inject
	private HttpRequestExecutor executor;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		executor.init();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!executor.service(request, response)){
			chain.doFilter(request, response);
		};
	}

	@Override
	public void destroy() {
		executor.destroy();
	}
	
	
}
