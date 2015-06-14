package com.otogami.web.view;

import java.util.List;

public interface TemplatePlugin {

	List<String> getExtensions();
	
	Class<? extends TemplateRenderer> getRenderer();
	
}
