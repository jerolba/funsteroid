package com.otogami.freemarker;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * Interface created to avoid circular dependencies between FreemarkerTemplateRenderer and DecoratorMacro
 * and construction problems in Guice
 * 
 */

public interface TemplateRendererInterface {

	void render(String template, Map<String, Object> model, OutputStream os);

	void render(String template, Map<String, Object> model, Writer writer);

}