package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class ReturnPointerInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackException, StackOverflowException {
		int returnValue = vm.stack.popInt();
		int address = vm.stack.discardStackFrame();
		vm.stack.pushInt(returnValue);

		vm.instructionList.jump(address);
	}

}
