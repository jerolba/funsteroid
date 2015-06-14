package com.otogami.freemarker.viewlet;

import java.util.Map;

public interface Viewlet {
	
	ModeletAndView control(Map<String,Object> params);
	
}