package com.otogami.guice;

import javax.inject.Provider;

import com.google.inject.AbstractModule;
import com.otogami.freemarker.FreemarkerModule;
import com.otogami.freemarker.MacroRegister;
import com.otogami.jackson.JacksonModule;
import com.otogami.web.InstanceFactory;
import com.otogami.web.funfilter.FilterConfiguration;
import com.otogami.web.resolver.ResolverChain;

public class FunsteroidModule extends AbstractModule {

	private Class<? extends Provider<? extends FilterConfiguration>> filter;
	private FilterConfiguration filterConfiguration;
	
	private Class<? extends Provider<? extends ResolverChain>> route;
	private Provider<? extends ResolverChain> routeProvider;

	private Class<? extends MacroRegister> macroRegister;
	private MacroRegister macroRegisterInstance;
	
	public FunsteroidModule(){
		
	}
	
	public FunsteroidModule withFilter(Class<? extends Provider<? extends FilterConfiguration>> filter){
		this.filter=filter;
		return this;
	}
	
	public FunsteroidModule withFilter(FilterConfiguration filterConfiguration){
		this.filterConfiguration=filterConfiguration;
		return this;
	}

	
	public FunsteroidModule withRoutes(Class<? extends Provider<? extends ResolverChain>> route){
		this.route=route;
		return this;
	}
	
	public FunsteroidModule withRoutes(Provider<? extends ResolverChain> routeProvider){
		this.routeProvider=routeProvider;
		return this;
	}
	
	public FunsteroidModule withMacros(Class<? extends MacroRegister> macroRegister){
		this.macroRegister=macroRegister;
		return this;
	}
	
	public FunsteroidModule withMacros(MacroRegister macroRegisterInstance){
		this.macroRegisterInstance=macroRegisterInstance;
		return this;
	}

	
	public FunsteroidModule(Class<? extends Provider<? extends FilterConfiguration>> filter,
			Class<? extends Provider<? extends ResolverChain>> route,
			Class<? extends MacroRegister> macroRegister){
		this.filter=filter;
		this.route=route;
		this.macroRegister=macroRegister;
	}
	
	@Override
	protected void configure() {
		
		bind(InstanceFactory.class).to(GuiceInstanceFactory.class).asEagerSingleton();
		
		if (filter!=null){
			bind(FilterConfiguration.class).toProvider(filter);
		}else if (filterConfiguration!=null){
			bind(FilterConfiguration.class).toInstance(filterConfiguration);
		}else{
			bind(FilterConfiguration.class).toInstance(new FilterConfiguration());
		}
		if (route!=null){
			bind(ResolverChain.class).toProvider(route);
		}else if (routeProvider!=null){
			bind(ResolverChain.class).toProvider(routeProvider);
		}else{
			bind(ResolverChain.class).toProvider(new Provider<ResolverChain>() {
				@Override
				public ResolverChain get() {
					return new ResolverChain();
				}
			});
		}
		
		if (macroRegisterInstance!=null){
			install(new FreemarkerModule(macroRegisterInstance));
		}else{
			install(new FreemarkerModule(macroRegister));
		}
		install(new JacksonModule());
	}
}

