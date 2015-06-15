package com.otogami.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.otogami.web.InstanceFactory;
import com.otogami.web.reflection.BindParamInfo;
import com.otogami.web.reflection.ClassExplorer;
import com.otogami.web.results.Result;

@Singleton
public class ClassControllerExecutor {

	
	private InstanceFactory instanceFactory;
	private ClassExplorer classExplorer;
	private ParameterBinder binder=new ParameterBinder();
	
	@Inject
	public ClassControllerExecutor(InstanceFactory instanceFactory, ClassExplorer classExplorer){
		this.instanceFactory=instanceFactory;
		this.classExplorer=classExplorer;
	}
	
	public Result execute(ControllerHolder classController,ServletRequest request, ServletResponse response){
		Class<?> controllerClass=classController.getClassControl();
		Method method=classController.getMethodClass();
		List<BindParamInfo> paramsInfo = classExplorer.getMethodParams(method);
		
		Object controller=instanceFactory.getInstance(controllerClass);
		
		Map<String,Object> pathParam=classController.getParams();
		try {
			Object[] args = createArgs(request,response,pathParam,method,paramsInfo);
			Object res=method.invoke(controller,args);
			return (Result) res;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private Object[] createArgs(ServletRequest request, ServletResponse response, Map<String,Object> pathParam, Method method, List<BindParamInfo> bindInfoLst){
		Method m=method;
		Class<?>[] paramTypes=m.getParameterTypes();
		Object[] params=new Object[paramTypes.length];
		int cont=0;
		for (BindParamInfo bindParamInfo : bindInfoLst) {
			String paramId=bindParamInfo.getParamId();
			Object value = null;
			if (paramId!=null){
				value=extractParam(request, pathParam, bindParamInfo);
			}else{
				value=extractParamFromServlet(request, response, bindParamInfo);
			}
			Object p=binder.convert(value,bindParamInfo.getParamType());
			params[cont]=p;
			cont++;
		}
		return params;
	}

	private Object extractParam(ServletRequest request, Map<String, Object> pathParam, BindParamInfo bindParamInfo) {
		String paramId=bindParamInfo.getParamId();
		if (paramId!=null){
			switch (bindParamInfo.getBindFrom()) {
			case QUERY:
					return request.getParameter(paramId);
			case PATH:
					return pathParam.get(paramId);
			case COOKIE:
					return CookieHelper.getCookieValue(((HttpServletRequest) request).getCookies(),paramId);
			case FORM:
				return request.getParameter(paramId);
			default:
				break;
			}
		}
		return null;
	}
	
	private Object extractParamFromServlet(ServletRequest request, ServletResponse response, BindParamInfo bindParamInfo){
		Class<?> type=bindParamInfo.getParamType();
		if (type.isAssignableFrom(ServletRequest.class)){
			return request;
		}
		if (type.isAssignableFrom(ServletResponse.class)){
			return response;
		}
		return null;
	}
}
