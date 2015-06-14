package com.otogami.web.resolver;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.HttpMethod;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.otogami.web.controller.ControllerHolder;

/**
 * When resolving a route is time consuming, and resolution result doesn't change in short time,
 * you can cache the resolver.
 * 
 * For example: access to database to validate the existence of an element o its PK
 * Bad use: a simple String comparasion or split
 *   
 * @author jerolba
 *
 */
public class CachedRouteResolver implements RouteResolver{

	private RouteResolver routeResolver;
	
	private LoadingCache<String, ControllerHolder> loadingCache=null;
	
	public CachedRouteResolver(RouteResolver routeResolverToUse,int min,int max,int minutes){
		this.routeResolver=routeResolverToUse;
		loadingCache=CacheBuilder.newBuilder().
				initialCapacity(min).
				maximumSize(max).
				expireAfterAccess(minutes, TimeUnit.MINUTES).
				build(new CacheLoader<String,ControllerHolder>(){
					@Override
					public ControllerHolder load(String key) throws Exception {
						return routeResolver.resolve(key,HttpMethod.GET);
					}
				});
	}
	
	public CachedRouteResolver(RouteResolver routeResolver){
		this(routeResolver,1000,2000,20);
	}
	
	@Override
	public ControllerHolder resolve(String route, String httpMethod) {
		try {
			if (HttpMethod.GET.equals(httpMethod)){
				return loadingCache.get(route);
			}
			return routeResolver.resolve(route,httpMethod);
		} catch (InvalidCacheLoadException icle){
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> explain() {
		return routeResolver.explain();
	}

}
