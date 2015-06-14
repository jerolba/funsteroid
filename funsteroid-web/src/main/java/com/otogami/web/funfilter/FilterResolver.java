package com.otogami.web.funfilter;

public interface FilterResolver {

	public Class<? extends Funfilter> filter(String url);
	
}
