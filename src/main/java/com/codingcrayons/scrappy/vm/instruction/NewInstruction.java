package com.codingcrayons.scrappy.vm.instruction;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;

public class NewInstruction extends Instruction {

	private static Logger logger = Logger.getLogger(NewInstruction.class);

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, HeapOutOfMemoryException {
		String className = params[0];
		logger.debug("Creating new class: " + className);
		SvmClass clazz = vm.permGenSpace.getClass(className);
		int pointer = vm.heap.alloc(clazz);
		vm.stack.pushPointer(pointer);
	}

}
