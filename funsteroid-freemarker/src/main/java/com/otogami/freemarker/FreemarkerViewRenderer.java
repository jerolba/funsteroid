package com.otogami.freemarker;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.otogami.freemarker.viewlet.MapWrapper;
import com.otogami.web.view.ModelAndView;
import com.otogami.web.view.TemplateRenderer;

public class FreemarkerViewRenderer implements TemplateRenderer {

	public static final String ROOT_MODEL = "_ROOT_MODEL";

	@Inject
	private TemplateRendererInterface freemarkerTemplateRenderer;

	@Override
	public void render(ModelAndView modelAndView, Writer writer) {
		Map<String, Object> rootModel = new HashMap<String, Object>();
		rootModel.putAll(modelAndView.getModel());
		modelAndView.getModel().put(ROOT_MODEL, new MapWrapper(rootModel));
		freemarkerTemplateRenderer.render(modelAndView.getTemplate(), modelAndView.getModel(), writer);
	}


}
