package com.otogami.freemarker.viewlet;

import java.util.Map;

public class MapWrapper {

	private Map map;
	
	public MapWrapper(Map map){
		this.map=map;
	}
	
	public Map get(){
		return map;
	}
}
