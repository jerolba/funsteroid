package com.otogami.jackson;

import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.map.ObjectMapper;

import com.otogami.web.JsonRenderer;

public class JacksonRenderer implements JsonRenderer{

	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void writeTo(Object model, OutputStream os) {
		try {
			mapper.writeValue(os,model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
