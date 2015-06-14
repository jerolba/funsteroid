package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class DoLayoutMacro implements TemplateDirectiveModel{

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		TemplateModel tm=env.getVariable("_doLayout_");
		StringModel strModel=(StringModel) tm;
		ByteArrayOutputStream baos=(ByteArrayOutputStream) strModel.getWrappedObject();
		
		env.getOut().write(new String(baos.toByteArray()));
	}

}
