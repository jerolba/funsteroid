package com.otogami.web.resolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.ws.rs.HttpMethod;

import com.otogami.web.controller.ClassControllerHolder;
import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.controller.LambdaController;
import com.otogami.web.controller.LambdaControllerHolder;
import com.otogami.web.reflection.ReflectionUtil;

public class SimpleMapResolver implements RouteResolver {

	private String httpMethod = null;
	private Route route;
	
	public SimpleMapResolver(String route, Class<?> controller) {
		this(route, null, controller, (String)null);
	}

	public SimpleMapResolver(String route, Class<?> controller, String method) {
		this(route, null, controller, method);
	}

	public SimpleMapResolver(String route, String httpMethod, Class<?> controller) {
		this(route, httpMethod, controller, (String) null);
	}
	
	public SimpleMapResolver(String route, String httpMethod, LambdaController controller){
		this.httpMethod = httpMethod;
		this.route = new SimpleRoute(route,new LambdaControllerHolder(controller));
	}

	public SimpleMapResolver(String route, String httpMethod, Class<?> controller, String method) {
		Method m=null;
		if (method!=null){
			m=ReflectionUtil.findMethod(controller, method, httpMethod);
		}else if (httpMethod!=null){
			m=ReflectionUtil.findFirstMethodWith(controller,httpMethod);
		}else{
			m=ReflectionUtil.findFirstMethodWith(controller,HttpMethod.GET);
		}
		create(route, httpMethod, controller, m);
	}
	
	public SimpleMapResolver(String route, String httpMethod, Class<?> controller, Method m) {
		create(route, httpMethod, controller, m);
	}

	private void create(String route, String httpMethod, Class<?> controller, Method m){
		this.httpMethod = httpMethod;
		if (DynamicRoute.isDynamic(route)){
			this.route = new DynamicRoute(route, controller, m);
		}else{
			this.route = new SimpleRoute(route, new ClassControllerHolder(controller, m));
		}
	}
	
	@Override
	public ControllerHolder resolve(String route, String httpMethod) {
		if (!match(this.httpMethod, httpMethod)) {
			return null;
		}
		
		ControllerHolder match=this.route.match(route);
		if (match!=null) {
			return match;
		}
		return null;
	}

	public List<String> explain() {
		return Arrays.asList(Explainer.explain(httpMethod, route.getRoute()));
	}

	public static boolean match(String method, String request){
		return (method==null) || method.equals(request);
	}
	
	private static interface Route{
		ControllerHolder match(String path);
		String getRoute();
	}
	
	private static class SimpleRoute implements Route {
		
		private String route;
		private ControllerHolder holder;
		
		public SimpleRoute(String route, ControllerHolder holder){
			this.route=route;
			this.holder=holder;
		}
		
		public ControllerHolder match(String path){
			if (path.equals(route)){
				return holder;
			};
			return null;
		}
		
		public String getRoute(){
			return route;
		}
	}
	
	private static class DynamicRoute implements Route {
		
		private String route;
		private Class<?> controller;
		private Method method;
		
		private String[] splitedRoute;
		private String[] paramNames;
		
		public DynamicRoute(String route, Class<?> controller, Method method){
			this.route=route;
			this.controller=controller;
			this.method=method;
			
			splitedRoute = split(route);
			paramNames=findParamNames(splitedRoute);
		}
		
		private static String[] split(String route){
			if (route.startsWith("/")){
				return route.substring(1).split("/");
			}
			return route.split("/");
		}
		
		private static String[] findParamNames(String[] splitedRoute){
			String[] res=new String[splitedRoute.length];
			for(int i=0;i<splitedRoute.length;i++){
				String part=splitedRoute[i];
				if (part.startsWith("{") && part.endsWith("}")){
					res[i]=part.substring(1, part.length()-1);
				}
			}
			return res;
		}
		
		public ControllerHolder match(String path){
			String[] partials=split(path);
			if (partials.length!=splitedRoute.length){
				return null;
			}
			if (matchParts(partials)){
				Map<String, Object> params = buildParams(partials);
				return new ClassControllerHolder(controller, method, params);
			}
			return null;
		}
		
		private boolean matchParts(String[] partials){
			for (int i=0;i<partials.length;i++){
				if (paramNames[i]==null && !partials[i].equals(splitedRoute[i])){
					return false;
				}
			}
			return true;
		}
		
		private Map<String,Object> buildParams(String[] partials){
			Map<String,Object> params=new HashMap<String,Object>();
			for (int i=0;i<partials.length;i++){
				if (paramNames[i]!=null){
					params.put(paramNames[i], partials[i]);
				}
			}
			return params;
		}
		
		public static boolean isDynamic(String route){
			return Stream.of(findParamNames(split(route)))
					.filter(str->str!=null)
					.findAny().isPresent();
		}
		
		public String getRoute(){
			return route;
		}
	}
}
