package com.otogami.freemarker.viewlet;

/**
 * 
 * Abstraction over Injector to avoid Guice or other DI engine
 * 
 */
public interface ViewletFactory {
	Viewlet getViewlet(String name);
}
