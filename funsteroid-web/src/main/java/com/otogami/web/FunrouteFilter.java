package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.funfilter.FilterChainElement;
import com.otogami.web.funfilter.FilterConfiguration;
import com.otogami.web.funfilter.FilterFinder;
import com.otogami.web.resolver.RouteMappingConfig;
import com.otogami.web.resolver.RouteResolver;
import com.otogami.web.resolver.RouterTrace;

@Singleton
public class FunrouteFilter implements Filter {
	
	private RouteResolver centralResolver;
	
	@Inject
	private FilterFinder filterFinder;
	
	@Inject
	private ChainFilterExecutor executor;
	
	@Inject 
	private Provider<RouteMappingConfig> routeConfigProvider;
	
	@Inject 
	private Provider<FilterConfiguration> filterConfigProvider; 

	
	public void init(FilterConfig filterConfig) throws ServletException {
		loadRoutesConfig();
		loadFunFilterConfig();
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		try{
			String uri=getRequestPath(httpRequest);
			String method=httpRequest.getMethod();
			ControllerHolder controllerHolder=centralResolver.resolve(uri,method);
			if (controllerHolder!=null){
				FilterChainElement chainFilter=filterFinder.getFilters(uri);
				executor.execute(chainFilter, controllerHolder, request, response);
				return;
			}
		}catch (RuntimeException th){
			th.printStackTrace();
			throw th;
		}
		chain.doFilter(httpRequest, response);
	}

	private void loadRoutesConfig(){
		RouteMappingConfig mapping = routeConfigProvider.get();
		centralResolver=mapping.getConfig();
		System.out.println(RouterTrace.trace(centralResolver.explain()));
	}
	
	private void loadFunFilterConfig(){
		FilterConfiguration filterConfig = filterConfigProvider.get();
		filterFinder.loadFilters(filterConfig.getResolvers());
	}
	
	public void destroy() {
		
	}
	
	private String getRequestPath(HttpServletRequest httpRequest){
		String uri=httpRequest.getRequestURI();
		String contextPath=httpRequest.getContextPath();
		return uri.substring(contextPath.length());
	}
}
