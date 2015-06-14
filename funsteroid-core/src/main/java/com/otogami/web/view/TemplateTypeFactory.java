package com.otogami.web.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.otogami.web.InstanceFactory;

@Singleton
public class TemplateTypeFactory {

	@Inject
	private InstanceFactory injector;
	
	private Map<String, TemplatePlugin> plugins=new HashMap<String, TemplatePlugin>();
	
	public TemplateTypeFactory(){
	}
	
	public void registerPlugin(TemplatePlugin plugin){
		for (String ext: plugin.getExtensions()) {
			plugins.put("."+ext, plugin);
		}
	}
	
	public TemplateRenderer getRenderer(String template){
		Set<String> pluginsExt=plugins.keySet();
		for (String ext: pluginsExt) {
			if (template.endsWith(ext)){
				TemplatePlugin plug=plugins.get(ext);
				Class<? extends TemplateRenderer> renderer=plug.getRenderer();
				return injector.getInstance(renderer);
			}
		}
		return null;
	}
}
