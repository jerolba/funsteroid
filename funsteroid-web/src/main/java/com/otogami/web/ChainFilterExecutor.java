package com.otogami.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.otogami.web.controller.ClassControllerExecutor;
import com.otogami.web.controller.ControllerHolder;
import com.otogami.web.funfilter.FilterChainElement;
import com.otogami.web.results.Result;
import com.otogami.web.results.ResultDispatcher;

@Singleton
public class ChainFilterExecutor {
	
	@Inject 
	private ClassControllerExecutor classControllerExecutor;
	
	@Inject
	private InstanceFactory injector;
	
	public void execute(FilterChainElement chainFilter, ControllerHolder classController, ServletRequest request, ServletResponse response) throws IOException{
		if (chainFilter!=null){
			chainFilter.append(new FinalChainElement());
		}else{
			chainFilter=new FinalChainElement();
		}
		System.out.println("Executing "+chainFilter);
		Result res=chainFilter.doFilter(classController, request, response);
		res.accept(new ResultDispatcher(injector,response));
	}
	
	public FilterChainElement getFinalChainElement(){
		return new FinalChainElement();
	}
	
	private class FinalChainElement extends FilterChainElement{

		public FinalChainElement() {
			super(null, null);
		}
		
		public Result doFilter(ControllerHolder classController, ServletRequest request, ServletResponse response){
			return classControllerExecutor.execute(classController, request, response);
		}
		
	}
}
