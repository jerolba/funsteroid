package com.otogami.guice;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.otogami.web.InstanceFactory;

public class GuiceInstanceFactory implements InstanceFactory{

	@Inject
	private Injector injector;

	@Override
	public <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}
	
}
