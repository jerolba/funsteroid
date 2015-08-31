package com.otogami.jackson;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otogami.web.JsonRenderer;

public class JacksonRenderer implements JsonRenderer{

	private static final JsonFactory factory = new JsonFactory(new ObjectMapper());
	
	
	@Override
	public void writeTo(Object model, OutputStream os) {
		try {
			JsonGenerator generator = factory.createGenerator(os);
			generator.writeObject(model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
