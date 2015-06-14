package com.otogami.web.view;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

	private Map<String,Object> model=new HashMap<String, Object>();

	private String template;
	
	public ModelAndView(String template){
		super();
		this.template=template;
	}
	
	public ModelAndView(String template, Map<String,Object> model){
		this.model.putAll(model);
		this.template=template;
	}
	
	public String getTemplate(){
		return template;
	}
	
	public void put(String key,Object value){
		this.model.put(key, value);
	}
	
	public Map<String,Object> getModel(){
		return model;
	}

}
