package com.otogami.web.results;

import java.io.IOException;

public class OkWithBinary implements Result {

	public ResultType getType() {
		return ResultType.OkWithBinary;
	}
	
	private byte[] content;
	private String contentType;
	
	public OkWithBinary(byte[] content, String contentType){
		this.content=content;
		this.contentType=contentType;
	}
	
	public byte[] getContent(){
		return content;
	}

	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}

	public String getContentType() {
		return contentType;
	}

}
