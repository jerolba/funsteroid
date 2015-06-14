package com.otogami.web.results;

import java.io.IOException;

public interface Result {

	ResultType getType();
	
	void accept(ResultVisitor visitor) throws IOException;
	
}
