package com.otogami.web.results;

import java.io.IOException;

public class OKJsonp extends OKJson{

	private String callback;

	@Override
	public ResultType getType() {
		return ResultType.Jsonp;
	}

	@Override
	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}

	public OKJsonp(String callback, Object model){
		super(model);
		this.callback=callback;
	}

	public OKJsonp(String callback, Object model, int status){
		super(model,status);
		this.callback=callback;

	}

	public String getCallback() {
		return callback;
	}

}
