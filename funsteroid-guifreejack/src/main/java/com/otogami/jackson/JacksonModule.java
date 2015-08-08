package com.otogami.jackson;

import com.google.inject.AbstractModule;
import com.otogami.jackson.JacksonRenderer;
import com.otogami.web.JsonRenderer;

public class JacksonModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(JsonRenderer.class).to(JacksonRenderer.class);
	}

}
