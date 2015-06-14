package com.otogami.freemarker.macro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.log.Logger;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TraceMacro implements TemplateDirectiveModel{

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		String templateName=env.getMainTemplate().getName();
		Logger logger = Logger.getLogger(templateName);
		try {
			String msg=getTraceIfLevelActive(logger, null, body);
			logger.debug(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getTraceIfLevelActive(Logger logger, String level,TemplateDirectiveBody body) throws Exception{
		if (logger.isDebugEnabled()){
			ByteArrayOutputStream baos=MacroHelper.renderInnerTag(body);
			return new String(baos.toByteArray());
		}
		return null;
	}
	
}
