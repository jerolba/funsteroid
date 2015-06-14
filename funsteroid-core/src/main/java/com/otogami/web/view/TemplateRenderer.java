package com.otogami.web.view;

import java.io.OutputStream;

public interface TemplateRenderer {

	void render(ModelAndView modelAndView, OutputStream os);
	
}
