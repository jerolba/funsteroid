package com.otogami.freemarker.viewlet;

import java.util.HashMap;
import java.util.Map;

public class ModeletAndView {

	private String template;
	private Map<String,Object> model=new HashMap<String, Object>();
	
	public ModeletAndView(String template){
		this.template=template;
	}
	
	public ModeletAndView(String template, Map<String,Object> model){
		this.template=template;
		this.model.putAll(model);
	}
	
	public void put(String key,Object value){
		this.model.put(key, value);
	}
	
	public String getTemplate(){
		return template;
	}
	
	public Map<String,Object> getModel(){
		return model;
	}
	
}
