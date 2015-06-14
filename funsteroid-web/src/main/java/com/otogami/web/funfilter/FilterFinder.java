package com.otogami.web.funfilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.otogami.web.InstanceFactory;

public class FilterFinder {

	private InstanceFactory injector;
	private List<FilterResolver> resolvers;
	
	@Inject
	public FilterFinder(InstanceFactory injector){
		this.injector=injector;
	}
	
	public void loadFilters(List<FilterResolver> resolvers){
		this.resolvers=resolvers;
	}
	
	public FilterChainElement getFilters(String url){
		List<Class<? extends Funfilter>> enableds=new ArrayList<Class<? extends Funfilter>>();
		for (FilterResolver resolver : resolvers) {
			Class<? extends Funfilter> filter = resolver.filter(url);
			if (filter!=null){
				enableds.add(filter);
			}
		}
		if (enableds.size()>0){
			FilterInstance instance=new FilterInstance(enableds);
			return instance.build();
		}
		return null;
	}
	
	private class FilterInstance{
		
		public List<Class<? extends Funfilter>> items;
		
		public FilterInstance(List<Class<? extends Funfilter>> items){
			this.items=items;
		}
		
		public FilterChainElement build(){
			return create(0);
		}
		
		private FilterChainElement create(int i){
			if (i==items.size()){
				return null;
			}
			FilterChainElement next=create(i+1);
			FilterChainElement el=new FilterChainElement(injector.getInstance(items.get(i)),next);
			return el;
		}
	}
}
