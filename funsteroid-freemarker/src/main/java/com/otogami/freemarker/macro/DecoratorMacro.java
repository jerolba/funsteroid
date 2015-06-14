package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import com.otogami.freemarker.FreemarkerViewRenderer;
import com.otogami.freemarker.TemplateRendererInterface;
import com.otogami.freemarker.viewlet.MapWrapper;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class DecoratorMacro implements TemplateDirectiveModel{

	public static final String decoratorVars="_DECORATORVARS_";
	
	private TemplateRendererInterface freemarkerTemplateRenderer;
	
	@Inject
	public DecoratorMacro(TemplateRendererInterface freemarkerTemplateRenderer){
		this.freemarkerTemplateRenderer=freemarkerTemplateRenderer;
	}
	
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		//Create a new var to inject in inner tag execution
		BeansWrapper wrapper=(BeansWrapper)(env.getConfiguration().getObjectWrapper());
		DecoratorVars vars=new DecoratorVars();
		env.setVariable(decoratorVars,new BeanModel(vars,wrapper));
		
		//Execute innter tag template
		ByteArrayOutputStream baos=MacroHelper.renderInnerTag(body);
		
		//Create a new model with macro params and string of innter tag execution
		Map<String,Object> paramToDecorator=MacroHelper.extractParams(params);
		paramToDecorator.put("_doLayout_", baos);
		paramToDecorator.putAll(vars.getValues());
		
		StringModel templateModel = (StringModel) env.getDataModel().get(FreemarkerViewRenderer.ROOT_MODEL);
		if (templateModel!=null){
			Map map = ((MapWrapper)templateModel.getWrappedObject()).get();
			paramToDecorator.putAll(map);
		}
		
		//Execute template 
		SimpleScalar strModelTpl=(SimpleScalar) params.get("tpl");
		String tplName=strModelTpl.getAsString();
		freemarkerTemplateRenderer.render(tplName, paramToDecorator, env.getOut());
	}
	
}
