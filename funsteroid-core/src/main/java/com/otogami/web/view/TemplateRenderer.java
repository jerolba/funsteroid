package com.otogami.web.view;

import java.io.Writer;

public interface TemplateRenderer {

	void render(ModelAndView modelAndView, Writer writer);
	
}
