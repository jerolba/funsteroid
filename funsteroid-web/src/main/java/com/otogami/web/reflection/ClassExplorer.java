package com.otogami.web.reflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

@Singleton
public class ClassExplorer {

	private Map<Method, List<BindParamInfo>> classInfoReporitory=new ConcurrentHashMap<Method, List<BindParamInfo>>();
	
	public ClassExplorer(){
	}
	
	public List<BindParamInfo> getMethodParams(Method method){
		List<BindParamInfo> paramsInfo = classInfoReporitory.get(method);
		if (paramsInfo==null){
			paramsInfo=ReflectionUtil.extractParamsInfo(method);
			classInfoReporitory.put(method, paramsInfo);
		}
		return paramsInfo;
	}

}
