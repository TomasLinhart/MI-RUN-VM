package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;

public class NewInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws ClassNotFoundException, StackOverflowException {
		String className = vm.instructionList.nextInstruction();
		SvmClass clazz = vm.permGenSpace.getClass(className);
		int pointer = vm.heap.alloc(clazz);
		vm.stack.pushPointer(pointer);
	}

}
