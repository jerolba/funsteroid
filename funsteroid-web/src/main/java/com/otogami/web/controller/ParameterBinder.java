package com.otogami.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ParameterBinder {

	private Map<String,Function<String,Object>> primitives=new HashMap<>();
	private Map<Class<?>,Function<String,Object>> binders=new HashMap<>();
	
	private interface Function<T, R> {

		R apply(T t);
	}
	
	public ParameterBinder(){
		binders.put(String.class,  new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				return v;
			}
		});
		binders.put(Integer.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return new Integer(v);
				}catch(NumberFormatException e){
					return null;
				}
			}
		});
		binders.put(Long.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return new Long(v);
				}catch(NumberFormatException e){
					return null;
				}
			}
		});
		binders.put(Double.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return new Double(v);
				}catch(NumberFormatException e){
					return null;
				}
			}
		});		
		binders.put(Float.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return new Float(v);
				}catch(NumberFormatException e){
					return null;
				}
			}
		});				
		binders.put(BigDecimal.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return new BigDecimal(v);
				}catch(NumberFormatException e){
					return null;
				}
			}
		});
		binders.put(Boolean.class, new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				return Boolean.getBoolean(v);
			}
		});

		
		primitives.put("int", new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return Integer.parseInt(v);
				}catch(NumberFormatException e){
					return 0;
				}
			}
		});
		primitives.put("long", new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return Long.parseLong(v);
				}catch(NumberFormatException e){
					return 0L;
				}
			}
		});		
		primitives.put("double", new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return Double.parseDouble(v);
				}catch(NumberFormatException e){
					return 0D;
				}
			}
		});
		
		primitives.put("float", new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				try{
					return Float.parseFloat(v);
				}catch(NumberFormatException e){
					return 0F;
				}
			}
		});
		
		primitives.put("boolean", new Function<String, Object>() {
			@Override
			public Object apply(String v) {
				return Boolean.parseBoolean(v);
			}
		});
		
	}
	
	public Object convert(Object value, Class<?> paramType) {
		if (value!=null && !(value instanceof String)){
			return value;
		}
		if (paramType==String.class){
			return value;
		}
		
		Function<String, Object> binder = binders.get(paramType);
		if (binder==null){
			String name = paramType.getName();
			binder = primitives.get(name);
			if (binder==null){
				return value;
			}
			return binder.apply((String)value);
		}
		return binder.apply((String)value);
	}

}
