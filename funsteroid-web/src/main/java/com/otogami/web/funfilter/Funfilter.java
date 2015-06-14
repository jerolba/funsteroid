package com.otogami.web.funfilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.results.Result;

public interface Funfilter {

	public Result doFilter(ControllerHolder classController, ServletRequest request, ServletResponse response, FilterChainElement chain);
	
}
