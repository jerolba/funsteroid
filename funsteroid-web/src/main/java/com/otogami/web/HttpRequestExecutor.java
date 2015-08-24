package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.funfilter.FilterChainElement;
import com.otogami.web.funfilter.FilterConfiguration;
import com.otogami.web.funfilter.FilterFinder;
import com.otogami.web.resolver.ResolverChain;
import com.otogami.web.resolver.RouteResolver;
import com.otogami.web.resolver.RouterTrace;

public class HttpRequestExecutor {

	private RouteResolver centralResolver;
	
	@Inject 
	private Provider<ResolverChain> routeConfigProvider;
	
	@Inject 
	private Provider<FilterConfiguration> filterConfigProvider; 

	@Inject
	private FilterFinder filterFinder;
	
	@Inject
	private ChainFilterExecutor executor;
	
	public void init() throws ServletException {
		loadRoutesConfig();
		loadFunFilterConfig();
	}
	
	public boolean service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		try{
			String uri=getRequestPath(httpRequest);
			String method=httpRequest.getMethod();
			ControllerHolder controllerHolder=centralResolver.resolve(uri,method);
			if (controllerHolder!=null){
				FilterChainElement chainFilter=filterFinder.getFilters(uri);
				executor.execute(chainFilter, controllerHolder, request, response);
				return true;
			}
			return false;
		}catch (RuntimeException th){
			th.printStackTrace();
			throw th;
		}
	}
	
	private void loadRoutesConfig(){
		centralResolver=routeConfigProvider.get();
		System.out.println(RouterTrace.trace(centralResolver.explain()));
	}
	
	private void loadFunFilterConfig(){
		FilterConfiguration filterConfig = filterConfigProvider.get();
		filterFinder.loadFilters(filterConfig.getResolvers());
	}
	
	private String getRequestPath(HttpServletRequest httpRequest){
		String uri=httpRequest.getRequestURI();
		String contextPath=httpRequest.getContextPath();
		return uri.substring(contextPath.length());
	}

	public void destroy() {
		
	}
}
