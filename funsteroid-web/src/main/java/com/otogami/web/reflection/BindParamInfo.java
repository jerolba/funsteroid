package com.otogami.web.reflection;


public class BindParamInfo {

	private BindFrom bindFrom;
	private String paramId;
	private Class paramType;
	
	public BindFrom getBindFrom() {
		return bindFrom;
	}
	public void setBindFrom(BindFrom bindFrom) {
		this.bindFrom = bindFrom;
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public Class getParamType() {
		return paramType;
	}
	public void setParamType(Class paramType) {
		this.paramType = paramType;
	}
	
}
