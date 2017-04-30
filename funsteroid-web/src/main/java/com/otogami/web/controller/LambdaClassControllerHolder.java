package com.otogami.web.controller;

public class LambdaClassControllerHolder implements ControllerHolder{

	private Class<? extends LambdaController> controller;
	
	public LambdaClassControllerHolder(Class<? extends LambdaController> controller) {
		this.controller=controller;
	}
	
	public Class<? extends LambdaController> getClassController(){
		return controller;
	}
	
	@Override
	public HolderType getType(){
		return HolderType.LambdaClassHolder;
	}

}
