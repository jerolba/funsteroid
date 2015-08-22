package com.otogami.web.funfilter;

public interface FilterResolver {

	Class<? extends Funfilter> filter(String url);
	
}
