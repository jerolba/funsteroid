package com.otogami.tomcat;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.google.inject.servlet.GuiceFilter;
import com.otogami.ServerConfig;
import com.otogami.guice.FunsteroidModule;
import com.otogami.guice.GuiceConfigListener;

public class TomcatServer extends ServerConfig<TomcatServer> {

	public void run() throws Exception{
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		
		final FunsteroidModule funsteroidModule=new FunsteroidModule()
				.withFilter(filterConfiguration)
				.withFilter(filter)
				.withRoutes(route)
				.withRoutes(routeProvider)
				.withMacros(macroRegister)
				.withMacros(macroRegisterInstance);
		
		
		Context webapp = tomcat.addWebapp("/", getWebAppPath());
		
		createGuiceFilter(webapp);
		
		webapp.addServletContainerInitializer(new ServletContainerInitializer() {
			
			@Override
			public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
				ctx.addListener(new GuiceConfigListener(funsteroidModule));
			}
		}, null);
		
		tomcat.start();
		tomcat.getServer().await();
	}
	
	private void createGuiceFilter(Context context) {
		FilterDef filterDefinition = new FilterDef();
		filterDefinition.setFilterName(GuiceFilter.class.getSimpleName());
		filterDefinition.setFilterClass(GuiceFilter.class.getName());
		context.addFilterDef(filterDefinition);

		FilterMap filterMapping = new FilterMap();
		filterMapping.setFilterName(GuiceFilter.class.getSimpleName());
		filterMapping.addURLPattern("/*");
		context.addFilterMap(filterMapping);
	}
	
	private String getWebAppPath(){
		if (resourcesPath==null){
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			File project = new File (s,".project");
			File classpath= new File (s,".classpath");
			if (project.exists() && classpath.exists()){
				return s+"/"+"src/main/webapp/"; 
			}
		}
		return resourcesPath;
	}
}
