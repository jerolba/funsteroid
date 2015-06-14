package com.otogami.web.results;

import java.io.IOException;

public class Redirect implements Result{

	private int status;
	private String url;
	
	@Override
	public ResultType getType() {
		return ResultType.Redirect;
	}
	
	public Redirect(int status, String url){
		this.status=status;
		this.url=url;
	}

	public int getStatus() {
		return status;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}

}
