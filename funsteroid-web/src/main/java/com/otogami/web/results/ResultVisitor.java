package com.otogami.web.results;

import java.io.IOException;

public interface ResultVisitor {

	void visit(Ok result) throws IOException;
	void visit(OkWithContent result) throws IOException;
	void visit(OkWithBinary result) throws IOException;
	void visit(OkWithInputStream result) throws IOException;
	void visit(Redirect result) throws IOException;
	void visit(OKJson result) throws IOException;
	void visit(OKJsonp okJsonp) throws IOException;

}
