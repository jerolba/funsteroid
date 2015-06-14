package com.otogami.freemarker;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import javax.inject.Named;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

public class GlobalVariables {

	private final static String defaultGlobalVarsFile="TemplateVars.properties";

	@Named("globalVarFreemarker")
	private String globalVars=defaultGlobalVarsFile;

	public void addGlobalVariables(Configuration cfg){
		Properties global=loadVars();
		Set<Object> keys=global.keySet();
		for (Object key : keys) {
			try {
				cfg.setSharedVariable((String)key,(String)global.get(key));
			} catch (TemplateModelException e) {
				e.printStackTrace();
			}
		}
	}

	private Properties loadVars(){
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(globalVars);
			if (is == null) {
				if (!defaultGlobalVarsFile.equals(globalVars)){
					//TODO: Replace with an error trace
					throw new FileNotFoundException(globalVars);
				}
			}else{
				Properties properties = new Properties();
				properties.load(is);
				return properties;
			}
		} catch (Exception e) {
			if (!defaultGlobalVarsFile.equals(globalVars)){
				e.printStackTrace();
			}
		}
		return new Properties();
	}
}
