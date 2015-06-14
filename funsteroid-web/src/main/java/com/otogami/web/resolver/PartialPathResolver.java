package com.otogami.web.resolver;

import java.util.ArrayList;
import java.util.List;

import com.otogami.web.controller.ControllerHolder;

public class PartialPathResolver implements RouteResolver {

	private ResolverChain chain=new ResolverChain();

	private String prefix;
	private int len;
	
	public PartialPathResolver(String prefix){
		this.prefix=prefix;
		this.len=prefix.length();
	}
	
	public void addResolver(RouteResolver resolver){
		chain.addResolver(resolver);
	}

	public void addResolver(ResolverChain resolver){
		chain.addResolver(resolver);
	}

	@Override
	public ControllerHolder resolve(String route, String httpMethod){
		if (route.startsWith(prefix)){
			return chain.resolve(route.substring(len), httpMethod);
		}		
		return null;
	}
	
	public List<String> explain() {
		List<String> res=new ArrayList<String>();
		List<RouteResolver> resolvers=chain.getResolvers();
		for (RouteResolver routeResolver : resolvers) {
			List<String> ret=routeResolver.explain();
			for (String subRoute: ret) {
				int idx=subRoute.indexOf("\t"); 
				String pref=subRoute.substring(0,idx+1);
				String suff=subRoute.substring(idx+1);
				res.add(pref+prefix+suff);
			}
		}
		return res;
	}

}
