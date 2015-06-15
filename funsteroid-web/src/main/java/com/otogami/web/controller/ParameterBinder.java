package com.otogami.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParameterBinder {

	private Map<String,Function<String,Object>> primitives=new HashMap<>();
	private Map<Class,Function<String,Object>> binders=new HashMap<>();
	
	public ParameterBinder(){
		binders.put(String.class,  (v) -> v);
		binders.put(Integer.class, (v) -> {
			try{
				return new Integer(v);
			}catch(NumberFormatException e){
				return null;
			}
		});
		binders.put(Long.class, (v) -> {
			try{
				return new Long(v);
			}catch(NumberFormatException e){
				return null;
			}
		});
		binders.put(Double.class, (v) -> {
			try{
				return new Double(v);
			}catch(NumberFormatException e){
				return null;
			}
		});		
		binders.put(Float.class, (v) -> {
			try{
				return new Float(v);
			}catch(NumberFormatException e){
				return null;
			}
		});				
		binders.put(BigDecimal.class, (v) -> {
			try{
				return new BigDecimal(v);
			}catch(NumberFormatException e){
				return null;
			}
		});
		binders.put(Boolean.class, (v) -> {
			return Boolean.getBoolean(v);
		});

		
		primitives.put("int", (v) -> {
			try{
				return Integer.parseInt(v);
			}catch(NumberFormatException e){
				return 0;
			}
		});
		primitives.put("long", (v) -> {
			try{
				return Long.parseLong(v);
			}catch(NumberFormatException e){
				return 0L;
			}
		});		
		primitives.put("double", (v) -> {
			try{
				return Double.parseDouble(v);
			}catch(NumberFormatException e){
				return 0D;
			}
		});
		
		primitives.put("float", (v) -> {
			try{
				return Float.parseFloat(v);
			}catch(NumberFormatException e){
				return 0F;
			}
		});
		
		primitives.put("boolean", (v) -> {
			return Boolean.parseBoolean(v);
		});
		
	}
	
	public Object convert(Object value, Class paramType) {
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
