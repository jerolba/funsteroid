package com.otogami.freemarker.config;

import java.util.HashSet;
import java.util.Set;

import com.otogami.freemarker.FreemarkerTemplateRenderer;
import com.otogami.freemarker.GlobalVariables;
import com.otogami.freemarker.TemplateRendererInterface;
import com.otogami.freemarker.viewlet.Viewlet;
import com.otogami.freemarker.viewlet.ViewletFactory;
import com.otogami.freemarker.viewlet.ViewletInvoker;

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;

public class FunsteroidFreemarkerConfiguration extends Configuration {

	private ViewletFactory viewletFactory;
	private TemplateRendererInterface templateRenderer;
	
	private Set<String> failedViewlets=new HashSet<String>();
	
	public FunsteroidFreemarkerConfiguration(GlobalVariables globalVariables, FunsteroidMacrosRegister funsteroidMacrosRegister, ViewletFactory viewletFactory){
		super(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		this.viewletFactory=viewletFactory;
		this.templateRenderer=new FreemarkerTemplateRenderer(this);
		globalVariables.addGlobalVariables(this);
		funsteroidMacrosRegister.register(this);
	}
	
    public TemplateModel getSharedVariable(String name) {
    	TemplateModel shared=super.getSharedVariable(name);
    	if (shared==null){
   			shared=getViewletFromFactory(name);
    	}
    	return shared;
    }
    
    private TemplateModel getViewletFromFactory(String name){
    	if (failedViewlets.contains(name)){
    		return null;
    	}
		Viewlet viewlet=viewletFactory.getViewlet(name);
		if (viewlet!=null){
			return new ViewletInvoker(viewlet, templateRenderer);
		}
		failedViewlets.add(name);
		return null;
    }
    
}
