package com.otogami.web.funfilter;

import java.util.ArrayList;
import java.util.List;

public class FilterConfiguration {

	private List<FilterResolver> configured=new ArrayList<FilterResolver>();
	
	public void addFilter(FilterResolver newOne){
		configured.add(newOne);
	}
	
	public List<FilterResolver> getResolvers(){
		return configured;
	}
	
}
