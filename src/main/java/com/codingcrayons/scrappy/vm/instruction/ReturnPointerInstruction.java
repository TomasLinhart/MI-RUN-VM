package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class ReturnPointerInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackException, StackOverflowException {
		int returnValue = vm.stack.popInt();
		int address = vm.stack.discardStackFrame();
		vm.stack.pushPointer(returnValue);

		vm.instructionList.jump(address);
	}

}
