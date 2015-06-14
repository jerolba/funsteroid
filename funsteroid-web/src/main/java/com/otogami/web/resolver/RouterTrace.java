package com.otogami.web.resolver;

import java.util.List;

public class RouterTrace {

	public static String trace(List<String> routes){
		StringBuilder sb=new StringBuilder();
		for (String rt : routes) {
			sb.append(rt).append("\n");
		}
		return sb.toString();
	}
}
