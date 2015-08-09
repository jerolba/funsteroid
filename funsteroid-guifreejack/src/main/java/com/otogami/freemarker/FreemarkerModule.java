package com.otogami.freemarker;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.otogami.freemarker.config.DefaultConfigurationProvider;
import com.otogami.freemarker.plug.FreemarkerPlugin;
import com.otogami.freemarker.viewlet.ViewletFactory;

import freemarker.template.Configuration;

public class FreemarkerModule extends AbstractModule{

	private Class<? extends MacroRegister> macroRegister;
	private MacroRegister macroRegisterInstance;
	
	public FreemarkerModule() {
	}
	
	public FreemarkerModule(Class<? extends MacroRegister> macroRegister) {
		this.macroRegister=macroRegister;
	}
	
	public FreemarkerModule(MacroRegister macroRegisterInstance) {
		this.macroRegisterInstance=macroRegisterInstance;
	}

	
	@Override
	protected void configure() {
		bind(Configuration.class).toProvider(DefaultConfigurationProvider.class).in(Scopes.SINGLETON);
		bind(TemplateRendererInterface.class).to(FreemarkerTemplateRenderer.class);
		bind(FreemarkerPlugin.class).asEagerSingleton();
		bind(ViewletFactory.class).to(GuiceViewletFactory.class);
		if (macroRegister!=null){
			bind(MacroRegister.class).to(macroRegister);
		}else if (macroRegisterInstance!=null){
			bind(MacroRegister.class).toInstance(macroRegisterInstance);
		}else{
			bind(MacroRegister.class).toInstance(cfg->{});
		}
	}

}
