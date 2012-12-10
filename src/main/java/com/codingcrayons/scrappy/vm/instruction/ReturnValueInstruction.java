package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class ReturnValueInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackException, StackOverflowException {
		byte[] retVal = vm.stack.popValue();
		int address = vm.stack.discardStackFrame();
		vm.stack.push(retVal);

		vm.instructionList.jump(address);
	}

}
