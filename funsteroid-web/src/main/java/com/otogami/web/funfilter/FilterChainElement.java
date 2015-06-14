package com.otogami.web.funfilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.results.Result;

public class FilterChainElement {

	private Funfilter funFilter;
	private FilterChainElement nextElement;
	
	public FilterChainElement(Funfilter funFilter, FilterChainElement nextElement){
		this.funFilter=funFilter;
		this.nextElement=nextElement;
	}
	
	public FilterChainElement getNextElement() {
		return nextElement;
	}
	
	public Result doFilter(ControllerHolder classController, ServletRequest request, ServletResponse response){
		return funFilter.doFilter(classController, request, response, this);
	}
	
	public void append(FilterChainElement element){
		if (nextElement!=null){
			nextElement.append(element);
		}else{
			nextElement=element;
		}
	}
	
}
