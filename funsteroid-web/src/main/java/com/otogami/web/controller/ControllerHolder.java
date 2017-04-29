package com.otogami.web.controller;

public interface ControllerHolder{

	public enum HolderType {ClassHolder,LambdaHolder, LambdaClassHolder};
	
	HolderType getType();
	
}
