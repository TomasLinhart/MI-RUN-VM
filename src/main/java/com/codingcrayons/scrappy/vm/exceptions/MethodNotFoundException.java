package com.codingcrayons.scrappy.vm.exceptions;

public class MethodNotFoundException extends ScrappyVmException {

	private static final long serialVersionUID = 1L;

	public MethodNotFoundException(String className, String methodName) {
		super("Method " + methodName + " not found for class " + className);
	}

}
