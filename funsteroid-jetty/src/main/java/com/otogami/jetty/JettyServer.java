package com.otogami.jetty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.inject.servlet.GuiceFilter;
import com.otogami.ServerConfig;
import com.otogami.guice.FunsteroidModule;
import com.otogami.guice.GuiceConfigListener;

public class JettyServer extends ServerConfig<JettyServer> {
	
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
