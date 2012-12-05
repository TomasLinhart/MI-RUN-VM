package com.codingcrayons.scrappy.vm.exceptions;

public class StackOverflowException extends ScrappyVmException {

	private static final long serialVersionUID = 1L;

	public StackOverflowException() {
		super("Stack size was exceeded");
	}

}
