package com.otogami.web.resolver;

import java.util.Set;

import com.otogami.web.reflection.PackageExplorer;

public class JaxRsPackageScanResolver extends ResolverChain {

	public JaxRsPackageScanResolver(Class exampleClass){
		this(exampleClass.getPackage().getName());
	}
	
	public JaxRsPackageScanResolver(String packageName){
		try {
			Set<Class<?>> classes = PackageExplorer.getClasses(packageName);
			classes.stream().forEach(c->addResolver(new JaxRsResolver(c)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
