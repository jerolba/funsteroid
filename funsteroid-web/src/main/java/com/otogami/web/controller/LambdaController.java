package com.otogami.web.controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.otogami.web.results.Result;

@FunctionalInterface
public interface LambdaController {
	
	Result execute(ServletRequest req, ServletResponse res);
	
}
