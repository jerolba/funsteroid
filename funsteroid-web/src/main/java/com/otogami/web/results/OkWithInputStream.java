package com.otogami.web.results;

import java.io.IOException;
import java.io.InputStream;

public class OkWithInputStream implements Result {

	public ResultType getType() {
		return ResultType.OkWithInputStream;
	}
	
	private InputStream inputStream;
	private String contentType;
	
	public OkWithInputStream(InputStream inputStream, String contentType){
		this.inputStream=inputStream;
		this.contentType=contentType;
	}
	
	public InputStream getInputStream(){
		return inputStream;
	}

	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}

	public String getContentType() {
		return contentType;
	}

}
