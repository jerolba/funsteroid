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
import com.otogami.web.controller.ControllerHolder.HolderType;
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
	
	public Result execute(ControllerHolder controllerHolder,ServletRequest request, ServletResponse response){
		if (controllerHolder.getType()==HolderType.ClassHolder){
			ClassControllerHolder classController=(ClassControllerHolder) controllerHolder;
			Method method=classController.getMethodClass();
			List<BindParamInfo> paramsInfo = classExplorer.getMethodParams(method);
			
			Object controller=instanceFactory.getInstance(classController.getClassControl());
			
			Map<String,Object> pathParam=classController.getParams();
			try {
				Object[] args = createArgs(request,response,pathParam,method,paramsInfo);
				Object res=method.invoke(controller,args);
				return (Result) res;
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}else if (controllerHolder.getType()==HolderType.LambdaHolder){
			LambdaControllerHolder lambdaController=(LambdaControllerHolder) controllerHolder;
			return lambdaController.getController().execute(request, response);
		}
		return null;
	}
	
	
	private Object[] createArgs(ServletRequest request, ServletResponse response, Map<String,Object> pathParam, Method method, List<BindParamInfo> bindInfoLst){
		Object[] params=new Object[method.getParameters().length];
		int cont=0;
		for (BindParamInfo bindParamInfo : bindInfoLst) {
			Object value=extractParam(request, response, pathParam, bindParamInfo);
			params[cont]=binder.convert(value,bindParamInfo.getParamType());
			cont++;
		}
		return params;
	}

	private Object extractParam(ServletRequest request, ServletResponse response, Map<String, Object> pathParam, BindParamInfo bindParamInfo) {
		String paramId=bindParamInfo.getParamId();
		if (paramId!=null){
			if (bindParamInfo.getBindFrom()!=null){
				switch (bindParamInfo.getBindFrom()) {
				case QUERY:
						return request.getParameter(paramId);
				case PATH:
						return pathParam.get(paramId);
				case COOKIE:
						return CookieHelper.getCookieValue(((HttpServletRequest) request).getCookies(),paramId);
				case FORM:
					return request.getParameter(paramId);
				case REQUEST:
					return request;
				case RESPONSE:
					return response;
				default:
					break;
				}
			}else{
				Object res = request.getParameter(paramId);
				if (res==null){
					res = pathParam.get(paramId);
				}
				return res;
			}
		}else{
			switch (bindParamInfo.getBindFrom()) {
				case REQUEST:
					return request;
				case RESPONSE:
					return response;
				default:
					break;
			}
		}
		return null;
	}
	
}
