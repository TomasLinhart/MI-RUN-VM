package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class NewArrayInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException {
		int size = Integer.parseInt(params[0]);
		int pointer = vm.heap.allocArray(size);
		vm.stack.pushPointer(pointer);
	}

}
