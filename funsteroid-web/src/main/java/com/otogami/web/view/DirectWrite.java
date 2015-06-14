package com.otogami.web.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.otogami.web.results.OkWithBinary;
import com.otogami.web.results.OkWithContent;
import com.otogami.web.results.OkWithInputStream;
import com.otogami.web.results.Redirect;

public class DirectWrite {

	public static void process(OkWithContent result, ServletResponse response) throws IOException{
		response.getOutputStream().write(result.getContent().getBytes());
	}
	
	public static void process(Redirect result, ServletResponse response){
		HttpServletResponse httpResponse=(HttpServletResponse) response;
		httpResponse.setStatus(result.getStatus());
		httpResponse.setHeader("Location", result.getUrl());
	}
	
	public static void process(OkWithBinary result, ServletResponse response) throws IOException{
		response.setContentType(result.getContentType());
		response.getOutputStream().write(result.getContent());
	}
	
	public static void process(OkWithInputStream result, ServletResponse response) throws IOException {
		response.setContentType(result.getContentType());
		OutputStream os=response.getOutputStream();
		InputStream is = result.getInputStream();
		byte[] buffer = new byte[1024 * 10];
		int len = 0;
		while ((len = is.read(buffer)) >= 0) {
			os.write(buffer, 0, len);
		}
		is.close();
	}
}
