package com.otogami.freemarker;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.otogami.freemarker.FreemarkerTemplateRenderer;
import com.otogami.freemarker.MacroRegister;
import com.otogami.freemarker.TemplateRendererInterface;
import com.otogami.freemarker.config.DefaultConfigurationProvider;
import com.otogami.freemarker.plug.FreemarkerPlugin;
import com.otogami.freemarker.viewlet.ViewletFactory;

import freemarker.template.Configuration;

public class FreemarkerModule extends AbstractModule{

	private Class<? extends MacroRegister> macroRegister;
	
	public FreemarkerModule() {
	}
	
	public FreemarkerModule(Class<? extends MacroRegister> macroRegister) {
		this.macroRegister=macroRegister;
	}
	
	@Override
	protected void configure() {
		bind(Configuration.class).toProvider(DefaultConfigurationProvider.class).in(Scopes.SINGLETON);
		bind(TemplateRendererInterface.class).to(FreemarkerTemplateRenderer.class);
		bind(FreemarkerPlugin.class).asEagerSingleton();
		bind(MacroRegister.class).to(getMacroRegister());		
		bind(ViewletFactory.class).to(GuiceViewletFactory.class);
	}

	private Class<? extends MacroRegister> getMacroRegister(){
		if (macroRegister==null){
			return EmptyMacroRegister.class;
		}
		return macroRegister;
	}
	
	private static class EmptyMacroRegister implements MacroRegister{
		@Override
		public void register(Configuration cfg) {
		}
	}
}
