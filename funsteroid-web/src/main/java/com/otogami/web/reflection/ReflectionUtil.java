package com.otogami.web.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
		return null;
	}
	
	public static List<BindParamInfo> extractParamsInfo(Method method) {
		Annotation[][] params=method.getParameterAnnotations();
		Class<?>[] paramsType=method.getParameterTypes();
		List<BindParamInfo> paramsMetadata=new ArrayList<BindParamInfo>(); 
		for (int i=0;i<params.length;i++) {
			BindParamInfo info=extractParameterInfo(params[i],paramsType[i]);
			paramsMetadata.add(info);
		}
		return paramsMetadata;
	}

	private static BindParamInfo extractParameterInfo(Annotation[] annotations, Class<?> paramType){
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
		return info;
	}
}
