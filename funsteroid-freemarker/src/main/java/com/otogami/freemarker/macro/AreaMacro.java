package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class AreaMacro implements TemplateDirectiveModel{

	private String varName;
	
	public AreaMacro(String varName){
		this.varName=varName;
	}
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		ByteArrayOutputStream baos=MacroHelper.renderInnerTag(body);
		BeanModel beanModel=(BeanModel)env.getVariable(DecoratorMacro.decoratorVars);
		DecoratorVars decoratorVars=(DecoratorVars)beanModel.getWrappedObject();
		decoratorVars.appendValue(varName, new String(baos.toByteArray()));
	}

}
