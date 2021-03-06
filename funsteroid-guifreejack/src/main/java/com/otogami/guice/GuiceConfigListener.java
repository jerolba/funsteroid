package com.otogami.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.otogami.web.FunrouteFilter;

public class GuiceConfigListener extends GuiceServletContextListener {

	private FunsteroidModule cfg;
	private Module[] otherModules;
	
	public GuiceConfigListener(FunsteroidModule cfg, Module ... otherModules) {
		this.cfg=cfg;
		this.otherModules=otherModules;
	}
	
	@Override
	protected Injector getInjector() {
		ServletModule servletModule=new ServletModule(){
			@Override
            protected void configureServlets() {
				filter("/*").through(FunrouteFilter.class);
			}
		};
		Module[] all=new Module[otherModules.length+2];
		all[0]=cfg;
		all[1]=servletModule;
		for(int i=0;i<otherModules.length;i++){
			all[i+2]=otherModules[i];
		}
		return Guice.createInjector(all);
	}

}