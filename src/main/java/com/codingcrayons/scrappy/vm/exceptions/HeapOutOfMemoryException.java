package com.codingcrayons.scrappy.vm.exceptions;

public class HeapOutOfMemoryException extends ScrappyVmException {

	private static final long serialVersionUID = 1L;

	public HeapOutOfMemoryException() {
		super("Not enought memory on heap");
	}

}
