package com.codingcrayons.scrappy.vm.exceptions;

public class UnknownInstructionException extends ScrappyVmException {

	private static final long serialVersionUID = 1L;

	public UnknownInstructionException(String instruction) {
		super("Unknown instruction: " + instruction);
	}

}
