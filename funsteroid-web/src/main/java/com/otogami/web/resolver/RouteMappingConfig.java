package com.otogami.web.resolver;

public class RouteMappingConfig {

	private ResolverChain central=new ResolverChain();
	
	public void addResolver(RouteResolver resolver){
		central.addResolver(resolver);
	}
	
	public RouteResolver getConfig(){
		return central;
	}
}
