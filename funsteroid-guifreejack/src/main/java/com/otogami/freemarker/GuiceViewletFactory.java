package com.otogami.freemarker;

import javax.inject.Inject;

import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.otogami.freemarker.viewlet.Viewlet;
import com.otogami.freemarker.viewlet.ViewletFactory;

public class GuiceViewletFactory implements ViewletFactory{

	private Injector injector;
	
	@Inject
	public GuiceViewletFactory(Injector injector){
		this.injector=injector;
	}
	
	@Override
	public Viewlet getViewlet(String name) {
		try{
			Key<Viewlet> k=Key.get(Viewlet.class, Names.named(name));
			Viewlet impl=injector.getInstance(k);
			return impl;
		}catch(ConfigurationException e){
			return null;
		}
	}
	
	public static void bind(Binder binder,Class<? extends Viewlet> clasz, String name){
		binder.bind(Viewlet.class).annotatedWith(Names.named(name)).to(clasz);		
	}

}
