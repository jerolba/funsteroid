package com.otogami.web.resolver;

import java.util.List;

import com.otogami.web.controller.ControllerHolder;

public interface RouteResolver {

	ControllerHolder resolve(String route, String httpMethod);
	
	List<String> explain();
	
}
