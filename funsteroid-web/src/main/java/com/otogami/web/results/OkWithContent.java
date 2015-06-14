package com.otogami.web.results;

import java.io.IOException;

public class OkWithContent implements Result {

	public ResultType getType() {
		return ResultType.OkWithContent;
	}
	
	private String content;
	
	public OkWithContent(String content){
		this.content=content;
	}
	
	public String getContent(){
		return content;
	}

	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}

}
