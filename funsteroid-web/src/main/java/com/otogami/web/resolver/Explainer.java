package com.otogami.web.resolver;

public class Explainer {

	public static String explain(String route){
		return explain(null, route);
	}
	
	public static String explain(String method, String route){
		if (method==null){
			return "*\t"+route;
		}
		return method+"\t"+route;
	}
}
