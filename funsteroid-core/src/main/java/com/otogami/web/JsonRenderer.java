package com.otogami.web;

import java.io.OutputStream;

public interface JsonRenderer {

	void writeTo(Object model, OutputStream os);
	
}
