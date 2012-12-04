package com.codingcrayons.scrappy.vm.exceptions;

public class ClassNotFoundException extends ScrappyVmException {

	private static final long serialVersionUID = 1L;

	public ClassNotFoundException(String className) {
		super("Class not found: " + className);
	}

}
