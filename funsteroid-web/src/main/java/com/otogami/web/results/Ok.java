package com.otogami.web.results;

import java.io.IOException;
import java.util.Map;

import com.otogami.web.view.ModelAndView;

public class Ok implements Result {

	private Integer status;
	private ModelAndView modelAndView;
	
	public ResultType getType() {
		return ResultType.Ok;
	}

	public Ok(String template){
		this.modelAndView=new ModelAndView(template);
	}
	
	public Ok(String template, Map<String,Object> model){
		this.modelAndView=new ModelAndView(template,model);
	}
	
	public Ok(String template, Map<String,Object> model, int status){
		this.modelAndView=new ModelAndView(template,model);
		this.setStatus(status);
	}

	public ModelAndView getModelAndView(){
		return modelAndView;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void put(String name,Object value){
		modelAndView.put(name, value);
	}

	public void accept(ResultVisitor visitor) throws IOException{
		visitor.visit(this);
	}
	
}
