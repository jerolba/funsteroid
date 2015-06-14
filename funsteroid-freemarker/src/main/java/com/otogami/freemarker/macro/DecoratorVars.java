package com.otogami.freemarker.macro;

import java.util.HashMap;
import java.util.Map;

public class DecoratorVars {

	private Map<String,String> attributes=new HashMap<String, String>();
	
	public void setValue(String attrName,String value){
		attributes.put(attrName, value);
	}
	
	public void appendValue(String attrName,String value){
		String prevValue=attributes.get(attrName);
		if (prevValue==null){
			attributes.put(attrName, value);
		}else{
			String concat=prevValue+"\n"+value;
			attributes.put(attrName, concat);
		}
	}
	
	public Map<String,String> getValues(){
		return attributes;
	}
}
