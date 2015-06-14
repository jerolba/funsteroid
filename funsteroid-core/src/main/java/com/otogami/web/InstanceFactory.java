package com.otogami.web;

/**
 * 
 * Abstraction over Injector to avoid Guice or other DI engine
 * 
 * @author jerolba
 *
 */
public interface InstanceFactory {

	<T> T getInstance(Class<T> type);
}
