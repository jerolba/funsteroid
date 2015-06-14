package com.otogami.web.results;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.otogami.web.InstanceFactory;
import com.otogami.web.plug.JsonRenderer;
import com.otogami.web.view.DirectWrite;
import com.otogami.web.view.TemplateView;

public class ResultDispatcher implements ResultVisitor {

	private InstanceFactory injector;
	private HttpServletResponse response;

	public ResultDispatcher(InstanceFactory injector, ServletResponse response){
		this.injector=injector;
		this.response=(HttpServletResponse) response;
	}

	@Override
	public void visit(Ok result) throws IOException{
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if (result.getStatus()!=null){
			response.setStatus(result.getStatus());
		}
		TemplateView templateView=injector.getInstance(TemplateView.class);
		ByteArrayOutputStream baos=new ByteArrayOutputStream(4096);
		templateView.render(result.getModelAndView(),baos);
		if (baos.size()>0){
			OutputStream os=response.getOutputStream();
			os.write(baos.toByteArray());
		}
	}

	@Override
	public void visit(OKJson result) throws IOException{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (result.getStatus()!=null){
			response.setStatus(result.getStatus());
		}
		JsonRenderer renderer = injector.getInstance(JsonRenderer.class);
		renderer.writeTo(result.getModel(), response.getOutputStream());
	}

	@Override
	public void visit(OKJsonp result) throws IOException{
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		if (result.getStatus()!=null){
			response.setStatus(result.getStatus());
		}
		OutputStream os=response.getOutputStream();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		os.write((result.getCallback()+"(").getBytes());
		JsonRenderer renderer = injector.getInstance(JsonRenderer.class);
		renderer.writeTo(result.getModel(), baos);
		os.write(baos.toByteArray());
		os.write(")".getBytes());
	}

	@Override
	public void visit(OkWithContent result) throws IOException{
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		DirectWrite.process(result, response);
	}

	@Override
	public void visit(OkWithBinary result) throws IOException{
		DirectWrite.process(result, response);
	}

	@Override
	public void visit(OkWithInputStream result) throws IOException{
		DirectWrite.process(result, response);
	}

	@Override
	public void visit(Redirect result) throws IOException{
		DirectWrite.process(result, response);
	}

}
