package com.otogami.web.results;

import java.io.IOException;

public class OKJson implements Result {

	private Object model;
	private Integer status;

	@Override
	public ResultType getType() {
		return ResultType.Json;
	}

	@Override
	public void accept(ResultVisitor visitor) throws IOException {
		visitor.visit(this);
	}

	public OKJson(){
	}

	public OKJson(Object model){
		this.model=model;
	}

	public OKJson(Object model, int status){
		this.model=model;
		this.status=status;
	}

	public Object getModel(){
		return model;
	}
	
	public Integer getStatus(){
		return status;
	}

}
