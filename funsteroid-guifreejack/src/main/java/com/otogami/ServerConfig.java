package com.otogami;

import javax.inject.Provider;

import com.otogami.freemarker.MacroRegister;
import com.otogami.web.funfilter.FilterConfiguration;
import com.otogami.web.resolver.ResolverChain;

@SuppressWarnings({"rawtypes","unchecked"})
public class ServerConfig<T extends ServerConfig>{

	protected int port=8080;
	protected String resourcesPath;
	
	protected Class<? extends Provider<? extends FilterConfiguration>> filter;
	protected FilterConfiguration filterConfiguration;
	
	protected Class<? extends Provider<? extends ResolverChain>> route;
	protected Provider<? extends ResolverChain> routeProvider;

	protected Class<? extends MacroRegister> macroRegister;
	protected MacroRegister macroRegisterInstance;
	
	public T withPort(int port){
		this.port=port;
		return (T) this;
	}
	
	public T withFilter(Class<? extends Provider<? extends FilterConfiguration>> filter){
		this.filter=filter;
		return (T) this;
	}
	
	public T withFilter(FilterConfiguration filterConfiguration){
		this.filterConfiguration=filterConfiguration;
		return (T) this;
	}
	
	public T withRoutes(Class<? extends Provider<? extends ResolverChain>> route){
		this.route=route;
		return (T)this;
	}
	
	public T withRoutes(Provider<? extends ResolverChain> routeProvider){
		this.routeProvider=routeProvider;
		return (T)this;
	}
	
	public T withMacros(Class<? extends MacroRegister> macroRegister){
		this.macroRegister=macroRegister;
		return (T)this;
	}
	
	public T withMacros(MacroRegister macroRegisterInstance){
		this.macroRegisterInstance=macroRegisterInstance;
		return (T)this;
	}

	public T withResourcesPath(String resourcesPath) {
		this.resourcesPath=resourcesPath;
		return (T)this;
	}
}
