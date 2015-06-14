package com.otogami.web.view;

import java.io.OutputStream;

import javax.inject.Inject;

public class TemplateView {
	
	@Inject 
	private TemplateTypeFactory templateTypeFactory; 
	
	public void render(ModelAndView modelAndView, OutputStream stream){
		String template=modelAndView.getTemplate();
		TemplateRenderer renderer=templateTypeFactory.getRenderer(template);
		if (renderer!=null){
			renderer.render(modelAndView, stream);
		}else{
			throw new RuntimeException("No renderer founded");
		}
	}
}
