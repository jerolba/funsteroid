package com.otogami.freemarker.config;

import javax.inject.Inject;
import javax.inject.Provider;

import com.otogami.freemarker.GlobalVariables;
import com.otogami.freemarker.MacroRegister;
import com.otogami.freemarker.viewlet.ViewletFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public class DefaultConfigurationProvider implements Provider<Configuration> {

	@Inject
	private GlobalVariables globalVariables;

	@Inject
	private ViewletFactory viewletFactory;
	
	@Inject
	private FunsteroidMacrosRegister funsteroidMacrosRegister;

	@Inject
	private MacroRegister macroRegister;
	
	@Override
	public Configuration get() {
		FunsteroidFreemarkerConfiguration cfg = new FunsteroidFreemarkerConfiguration(globalVariables, funsteroidMacrosRegister, viewletFactory);
		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
		cfg.setNumberFormat("0");
		cfg.setClassForTemplateLoading(getClass(), "/tpl");
		if (macroRegister!=null) {
			macroRegister.accept(cfg);
		}
		return cfg;
	}

}
