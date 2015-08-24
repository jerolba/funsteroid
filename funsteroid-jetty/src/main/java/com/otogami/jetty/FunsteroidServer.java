package com.otogami.jetty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.inject.servlet.GuiceFilter;
import com.otogami.freemarker.MacroRegister;
import com.otogami.guice.FunsteroidModule;
import com.otogami.web.funfilter.FilterConfiguration;
import com.otogami.web.resolver.ResolverChain;

public class FunsteroidServer {
	
	private int port=8080;
	private String resourcesPath;
	
	private Class<? extends Provider<? extends FilterConfiguration>> filter;
	private FilterConfiguration filterConfiguration;
	
	private Class<? extends Provider<? extends ResolverChain>> route;
	private Provider<? extends ResolverChain> routeProvider;

	private Class<? extends MacroRegister> macroRegister;
	private MacroRegister macroRegisterInstance;
	
	public FunsteroidServer withPort(int port){
		this.port=port;
		return this;
	}
	
	public FunsteroidServer withFilter(Class<? extends Provider<? extends FilterConfiguration>> filter){
		this.filter=filter;
		return this;
	}
	
	public FunsteroidServer withFilter(FilterConfiguration filterConfiguration){
		this.filterConfiguration=filterConfiguration;
		return this;
	}

	
	public FunsteroidServer withRoutes(Class<? extends Provider<? extends ResolverChain>> route){
		this.route=route;
		return this;
	}
	
	public FunsteroidServer withRoutes(Provider<? extends ResolverChain> routeProvider){
		this.routeProvider=routeProvider;
		return this;
	}
	
	public FunsteroidServer withMacros(Class<? extends MacroRegister> macroRegister){
		this.macroRegister=macroRegister;
		return this;
	}
	
	public FunsteroidServer withMacros(MacroRegister macroRegisterInstance){
		this.macroRegisterInstance=macroRegisterInstance;
		return this;
	}

	public FunsteroidServer withResourcesPath(String resourcesPath) {
		this.resourcesPath=resourcesPath;
		return this;
	}

	public Server run() throws Exception{
		Server server = new Server(port);
		
		FunsteroidModule funsteroidModule=new FunsteroidModule()
				.withFilter(filterConfiguration)
				.withFilter(filter)
				.withRoutes(route)
				.withRoutes(routeProvider)
				.withMacros(macroRegister)
				.withMacros(macroRegisterInstance);

		HandlerList handlers = new HandlerList();
		
		ResourceHandler resourceHandler=new ResourceHandler(){
			protected void doDirectory(HttpServletRequest request,HttpServletResponse response, Resource resource) throws IOException{
				 response.sendError(HttpStatus.NOT_FOUND_404);
			}
			
		};
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setResourceBase(getWebAppPath());
		
		WebAppContext context = new WebAppContext();
        //TODO: Locate path before setup depending on enviroment execution
        context.setResourceBase(getWebAppPath());
        
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        context.addEventListener(new GuiceConfigListener(funsteroidModule));
        context.addFilter(GuiceFilter.class, "/*", null);
        handlers.addHandler(resourceHandler);
        handlers.addHandler(context);
        
        server.setHandler(handlers);
        server.start();
        server.join();
        return server;
	}
	
	private String getWebAppPath(){
		if (resourcesPath==null){
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			File project = new File (s,".project");
			File classpath= new File (s,".classpath");
			if (project.exists() && classpath.exists()){
				return "src/main/webapp/"; 
			}
		}
		return resourcesPath;
	}
}
