package com.otogami.web.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import com.otogami.web.reflection.ReflectionUtil;

public class ClassControllerHolder implements ControllerHolder{

	private Class<?> classControl;
	
	private Method methodClass;
	
	private Map<String,Object> params=new HashMap<String,Object>();
	
	@Override
	public HolderType getType(){
		return HolderType.ClassHolder;
	}
	
	public ClassControllerHolder(Class<?> classControl){
		this(classControl, ReflectionUtil.findFirstMethodWith(classControl,HttpMethod.GET), null);
	}
	
	public ClassControllerHolder(Class<?> classControl, Map<String,Object> params){
		this(classControl,ReflectionUtil.findFirstMethodWith(classControl,HttpMethod.GET),params);
	}
	
	public ClassControllerHolder(Class<?> classControl, String methodName){
		this(classControl, ReflectionUtil.findMethod(classControl, methodName, null));
	}
	
	public ClassControllerHolder(Class<?> classControl, String methodName, Map<String,Object> params){
		this(classControl, ReflectionUtil.findMethod(classControl, methodName, null), params);
	}
	
	public ClassControllerHolder(Class<?> classControl, Method classMethod){
		this(classControl, classMethod, null);
	}
	
	public ClassControllerHolder(Class<?> classControl, Method methodClass, Map<String,Object> params){
		this.classControl=classControl;
		this.methodClass=methodClass;
		if (params!=null) this.params.putAll(params);
	}
	
	public Class<?> getClassControl(){
		return classControl;
	}
	
	public Method getMethodClass() {
		return methodClass;
	}

	public void addParam(String key,String value){
		params.put(key, value);
	}
	
	public Map<String,Object> getParams(){
		return params;
	}
	
}
