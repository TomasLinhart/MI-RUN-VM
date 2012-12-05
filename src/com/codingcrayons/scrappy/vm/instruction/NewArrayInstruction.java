package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class NewArrayInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws ClassNotFoundException, StackOverflowException {
		int size = Integer.parseInt(vm.instructionList.nextInstruction());
		int pointer = vm.heap.allocArray(size);
		vm.stack.pushPointer(pointer);
	}

}
