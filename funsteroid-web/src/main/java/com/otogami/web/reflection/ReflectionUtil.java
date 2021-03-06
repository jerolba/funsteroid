package com.otogami.web.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class ReflectionUtil {

	public static Method findMethod(Class<?> controller, String methodName, String httpMethod){
		if (methodName!=null){
			Method[] methods = controller.getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)){
					if (httpMethod!=null){
						String methodHas=getHttpMethod(method);
						if (httpMethod.equals(methodHas)){
							return method;
						}
					}else{
						return method;
					}
				}
			}
		}
		
		return null;
	}
	
	public static Method findFirstMethodWith(Class<?> controller, String httpMethod){
		Method[] methods = controller.getMethods();
		for (Method method : methods) {
			String methodHas=getHttpMethod(method);
			if (httpMethod.equals(methodHas)){
				return method;
			}
		}
		return null;
	}
	
	
	public static String getHttpMethod(Method method){
		if (method.getAnnotation(GET.class)!=null){
			return HttpMethod.GET;
		}
		if (method.getAnnotation(POST.class)!=null){
			return HttpMethod.POST;
		}
		if (method.getAnnotation(DELETE.class)!=null){
			return HttpMethod.DELETE;
		}
		if (method.getAnnotation(PUT.class)!=null){
			return HttpMethod.PUT;
		}
		if (method.getAnnotation(OPTIONS.class)!=null){
			return HttpMethod.OPTIONS;
		}
		if (method.getAnnotation(HEAD.class)!=null){
			return HttpMethod.HEAD;
		}		
		return null;
	}
	
	public static List<BindParamInfo> extractParamsInfo(Method method) {
		List<BindParamInfo> res = new ArrayList<>();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i=0;i<parameterTypes.length;i++) {
			res.add(ReflectionUtil.extractParameterInfo(parameterTypes[i],parameterAnnotations[i]));
		}
		return res;
//		return Stream.of(method.getParameters())
//				.map(ReflectionUtil::extractParameterInfo)
//				.collect(Collectors.toList());
	}

	private static BindParamInfo extractParameterInfo(Class<?> class1, Annotation[] annotations) {
		Class<?> paramType = class1;
		BindParamInfo info=new BindParamInfo();
		info.setParamType(paramType);
		for (Annotation annotation : annotations) {
			if (annotation instanceof QueryParam){
				String id=((QueryParam)annotation).value();
				info.setBindFrom(BindFrom.QUERY);
				info.setParamId(id);
			}else if (annotation instanceof PathParam){
				String id=((PathParam)annotation).value();
				info.setBindFrom(BindFrom.PATH);
				info.setParamId(id);
			}else if (annotation instanceof CookieParam){
				String id=((CookieParam)annotation).value();
				info.setBindFrom(BindFrom.COOKIE);
				info.setParamId(id);
			}else if (annotation instanceof FormParam){
				String id=((FormParam)annotation).value();
				info.setBindFrom(BindFrom.FORM);
				info.setParamId(id);
			}
		}
		if (info.getBindFrom()==null){
			if (ServletRequest.class.isAssignableFrom(paramType)){
				info.setBindFrom(BindFrom.REQUEST);
			}else if (ServletResponse.class.isAssignableFrom(paramType)){
				info.setBindFrom(BindFrom.RESPONSE);
			}
		}
		//JDK 1.7 has no param name
//		if (info.getParamId()==null){
//			info.setParamId(parameter.getName());
//		}
		return info;
	}
	
	public static String getPathAnnotation(Annotation annotation){
		if (annotation!=null){
			Path path=((Path)annotation);
			String beginsWith=path.value();
			if (!beginsWith.startsWith("/")){
				beginsWith="/"+beginsWith;
			}
			return beginsWith;
		}
		return "";
	}
}
