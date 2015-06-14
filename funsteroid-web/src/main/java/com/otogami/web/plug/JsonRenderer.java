package com.otogami.web.plug;

import java.io.OutputStream;

public interface JsonRenderer {

	void writeTo(Object model, OutputStream os);
	
}
