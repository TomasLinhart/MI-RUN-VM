package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;

public class NewInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException {
		String className = params[0];
		SvmClass clazz = vm.permGenSpace.getClass(className);
		int pointer = vm.heap.alloc(clazz);
		vm.stack.pushPointer(pointer);
	}

}
