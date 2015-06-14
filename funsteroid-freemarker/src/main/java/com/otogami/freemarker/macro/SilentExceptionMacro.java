package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class SilentExceptionMacro implements TemplateDirectiveModel{

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try{
			ByteArrayOutputStream baos=MacroHelper.renderInnerTag(body);
			env.getOut().write(new String(baos.toByteArray()));
		}catch(RuntimeException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
