package com.otogami.web.resolver;

import java.util.Set;

import javax.inject.Provider;

import com.otogami.web.reflection.PackageExplorer;

public class JaxRsPackageScanResolver extends ResolverChain {

	public JaxRsPackageScanResolver(Class<?> exampleClass){
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
	
	static public class JaxRsPackageScanResolverProvider implements Provider<ResolverChain>{

		private Class<?> [] classes;
		private String[] packages;
		
		public JaxRsPackageScanResolverProvider(Class<?> ...classes){
			this.classes=classes;
		}
		
		public JaxRsPackageScanResolverProvider(String ...packages){
			this.packages=packages;
		}
		
		@Override
		public ResolverChain get() {
			ResolverChain chain=new ResolverChain();
			if (packages!=null){
				for(String pack: packages){
					chain.addResolver(new JaxRsPackageScanResolver(pack));
				}
			}else if (classes!=null){
				for(Class<?> clasz: classes){
					chain.addResolver(new JaxRsPackageScanResolver(clasz));
				}
			}
			return chain;
		}
		
	}
}
