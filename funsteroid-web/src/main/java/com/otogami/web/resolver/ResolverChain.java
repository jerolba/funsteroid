package com.otogami.web.resolver;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.HttpMethod;

import com.google.common.collect.ImmutableList;
import com.otogami.web.controller.ControllerHolder;

public class ResolverChain implements RouteResolver{

	private List<RouteResolver> resolvers = new ArrayList<RouteResolver>();

	public void addResolver(RouteResolver resolver) {
		resolvers.add(resolver);
	}
	
	public void addResolver(ResolverChain resolvers){
		List<RouteResolver> all=resolvers.getResolvers();
		for (RouteResolver routeResolver : all) {
			addResolver(routeResolver);
		}
	}

	public ControllerHolder resolve(String route){
		return resolve(route,HttpMethod.GET);
	}
	
	@Override
	public ControllerHolder resolve(String route, String httpMethod) {
		for (RouteResolver  resolver: resolvers) {
			ControllerHolder controller=resolver.resolve(route,httpMethod);
			if (controller!=null){
				return controller;
			}
		}
		return null;
	}
	
	public List<RouteResolver> getResolvers(){
		return ImmutableList.copyOf(resolvers);
	}
	
	public List<String> explain() {
		List<String> res=new ArrayList<String>();
		for (RouteResolver routeResolver : resolvers) {
			List<String> ret=routeResolver.explain();
			if (ret!=null){
				res.addAll(ret);
			}
		}
		return res;
	}

}
