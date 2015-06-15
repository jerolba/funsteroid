package com.otogami.web.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.reflection.ReflectionUtil;

public class JaxRsResolver implements RouteResolver {

	private ResolverChain chain;
	
	public JaxRsResolver(Class clasz){
		parseClass(clasz);
	}
	
	private void parseClass(Class clasz){
		Annotation path = clasz.getAnnotation(Path.class);
		String beginsWith=ReflectionUtil.getPathAnnotation(path);
		if (beginsWith.length()==1){
			chain=new ResolverChain();
		}else{
			chain=new PartialPathResolver(beginsWith);
		}
		Method[] methods = clasz.getMethods();
		for (Method method : methods) {
			String httpMethod = getFor(method, GET.class, HttpMethod.GET);
			if (httpMethod==null){
				httpMethod = getFor(method, POST.class, HttpMethod.POST);
			}
			if (httpMethod==null){
				httpMethod = getFor(method, PUT.class, HttpMethod.PUT);
			}
			if (httpMethod==null){
				httpMethod = getFor(method, DELETE.class, HttpMethod.DELETE);
			}
			if (httpMethod!=null){
				Annotation pathMethod = method.getAnnotation(Path.class);
				String continueWith=ReflectionUtil.getPathAnnotation(pathMethod);
				chain.addResolver(new SimpleMapResolver(continueWith,httpMethod,clasz,method));
			}
		}
		
	}
	
	private String getFor(Method method, Class<? extends Annotation> type, String name){
		Annotation annotation = method.getAnnotation(type);
		if (annotation!=null){
			return name;
		}
		return null;
	}
	
	@Override
	public ControllerHolder resolve(String route, String httpMethod) {
		return chain.resolve(route, httpMethod);
	}

	@Override
	public List<String> explain() {
		return chain.explain();
	}

}
