package com.otogami.web.controller;

public class LambdaControllerHolder implements ControllerHolder{

	private LambdaController controller;
	
	public LambdaControllerHolder(LambdaController controller) {
		this.controller=controller;
	}
	
	public LambdaController getController(){
		return controller;
	}
	
	@Override
	public HolderType getType(){
		return HolderType.LambdaHolder;
	}

}
