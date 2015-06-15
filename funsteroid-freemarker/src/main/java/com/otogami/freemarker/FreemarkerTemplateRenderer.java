package com.otogami.freemarker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerTemplateRenderer implements TemplateRendererInterface {

	private Provider<Configuration> configuration;

	@Inject
	public FreemarkerTemplateRenderer(Provider<Configuration> configuration){
		this.configuration=configuration;
	}

	@Override
	public void render(String template, Map<String,Object> model, OutputStream os){
		OutputStreamWriter writer=new OutputStreamWriter(os);
		render(template,model,writer);
	}

	@Override
	public void render(String template, Map<String,Object> model, Writer writer){
		try {
			Template tpl=configuration.get().getTemplate(template);
			tpl.process(model,writer);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FreemarkerException(e);
		} catch (TemplateException e) {
			e.printStackTrace();
			throw new FreemarkerException(e);
		}
	}

}
