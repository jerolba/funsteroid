package com.otogami.freemarker.viewlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.otogami.freemarker.FreemarkerViewRenderer;
import com.otogami.freemarker.TemplateRendererInterface;
import com.otogami.freemarker.macro.MacroHelper;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.log.Logger;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ViewletInvoker implements TemplateDirectiveModel{
	
	private static final Logger logger = Logger.getLogger(ViewletInvoker.class.getCanonicalName());

	private Viewlet viewlet;
	private TemplateRendererInterface freemarkerTemplateRenderer;
	
	public ViewletInvoker(Viewlet viewlet,TemplateRendererInterface freemarkerTemplateRenderer){
		this.viewlet=viewlet;
		this.freemarkerTemplateRenderer=freemarkerTemplateRenderer;
	}
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try{
			Map<String,Object> paramsViewlet=MacroHelper.extractParams(params);
			if (body!=null){
				ByteArrayOutputStream baos=MacroHelper.renderInnerTag(body);
				paramsViewlet.put("renderedBody", new String(baos.toByteArray()).trim());
			}
			ModeletAndView mv=viewlet.control(paramsViewlet);
			if (mv!=null){
				StringModel templateModel = (StringModel) env.getDataModel().get(FreemarkerViewRenderer.ROOT_MODEL);
				if (templateModel!=null){
					Map map = ((MapWrapper)templateModel.getWrappedObject()).get();
					paramsViewlet.putAll(map);
				}
				
				paramsViewlet.putAll(mv.getModel());
				freemarkerTemplateRenderer.render(mv.getTemplate(), paramsViewlet, env.getOut());
			}
		}catch(RuntimeException e){
			logger.error("Exception executing "+viewlet.getClass()+". Error "+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
