package com.otogami.web.view;

import java.io.Writer;

import javax.inject.Inject;

public class TemplateView {
	
	@Inject 
	private TemplateTypeFactory templateTypeFactory; 
	
	public void render(ModelAndView modelAndView, Writer writer){
		String template=modelAndView.getTemplate();
		TemplateRenderer renderer=templateTypeFactory.getRenderer(template);
		if (renderer!=null){
			renderer.render(modelAndView, writer);
		}else{
			throw new RuntimeException("No renderer founded");
		}
	}
}
