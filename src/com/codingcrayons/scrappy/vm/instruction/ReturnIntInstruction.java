package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class ReturnIntInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackException, StackOverflowException {
		int returnPointer = vm.stack.popInt();
		int address = vm.stack.discardStackFrame();
		vm.stack.pushInt(returnPointer);

		vm.instructionList.jump(address);
	}

}
