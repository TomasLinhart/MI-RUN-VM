package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class PushPointerInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException {
		int pointer = Integer.parseInt(params[0]);
		vm.stack.pushPointer(pointer);
	}

}
