package com.otogami.web.reflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

@Singleton
public class ClassExplorer {

	private Map<Method, List<BindParamInfo>> methodInfoReporitory=new ConcurrentHashMap<Method, List<BindParamInfo>>();
	
	public List<BindParamInfo> getMethodParams(Method method){
		return methodInfoReporitory.computeIfAbsent(method,(m)->ReflectionUtil.extractParamsInfo(m));
	}

}
