package com.otogami.freemarker.config;

import javax.inject.Inject;

import com.otogami.freemarker.MacroRegister;
import com.otogami.freemarker.macro.AreaMacro;
import com.otogami.freemarker.macro.DecoratorMacro;
import com.otogami.freemarker.macro.DoLayoutMacro;
import com.otogami.freemarker.macro.SilentExceptionMacro;
import com.otogami.freemarker.macro.TraceMacro;
import com.otogami.web.InstanceFactory;

import freemarker.template.Configuration;

public class FunsteroidMacrosRegister implements MacroRegister{
	
	@Inject
	private InstanceFactory injector;
	
	@Override
	public void accept(Configuration cfg) {
		cfg.setSharedVariable("silence", new SilentExceptionMacro());
		cfg.setSharedVariable("trace", new TraceMacro());
		cfg.setSharedVariable("decorate", injector.getInstance(DecoratorMacro.class));
		cfg.setSharedVariable("doLayout", new DoLayoutMacro());		
		cfg.setSharedVariable("meta", new AreaMacro("metaheader"));
		cfg.setSharedVariable("css", new AreaMacro("cssheader"));
		cfg.setSharedVariable("script", new AreaMacro("scriptheader"));
		cfg.setSharedVariable("scriptFooter", new AreaMacro("scriptfooter"));
	}

}
