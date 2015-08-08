package com.otogami.web.resolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.HttpMethod;

import com.otogami.web.controller.ClassControllerHolder;
import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.controller.LambdaController;
import com.otogami.web.controller.LambdaControllerHolder;
import com.otogami.web.reflection.ReflectionUtil;

public class SimpleMapResolver implements RouteResolver {

	private String httpMethod = null;
	private String route;
	
	private ControllerHolder holder=null;

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
		this.route = route;
		holder=new LambdaControllerHolder(controller);
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
		this.httpMethod = httpMethod;
		this.route = route;
		holder=new ClassControllerHolder(controller, m);
	}
	
	public SimpleMapResolver(String route, String httpMethod, Class<?> controller, Method m) {
		this.httpMethod = httpMethod;
		this.route = route;
		holder=new ClassControllerHolder(controller, m);
	}

	@Override
	public ControllerHolder resolve(String route, String httpMethod) {
		if (!match(this.httpMethod, httpMethod)) {
			return null;
		}
		if (route.equals(this.route)) {
			return holder;
		}
		return null;
	}

	public List<String> explain() {
		return Arrays.asList(Explainer.explain(httpMethod, route));
	}

	public static boolean match(String method, String request){
		return (method==null) || method.equals(request);
	}
}
