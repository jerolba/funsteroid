package com.otogami.freemarker.plug;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.otogami.freemarker.FreemarkerViewRenderer;
import com.otogami.web.view.TemplatePlugin;
import com.otogami.web.view.TemplateRenderer;
import com.otogami.web.view.TemplateTypeFactory;

//In Guice: bind with .asEagerSingleton() and automatically register it self
//TODO: in spring?

@Singleton
public class FreemarkerPlugin implements TemplatePlugin{

	@Inject
	public FreemarkerPlugin(TemplateTypeFactory templateFactory){
		templateFactory.registerPlugin(this);
	}
	
	public List<String> getExtensions() {
		return Arrays.asList("tpl","ftl","tpl.html","ftl.html");
	}
	
	public Class<? extends TemplateRenderer> getRenderer(){
		return FreemarkerViewRenderer.class;
	}

}
