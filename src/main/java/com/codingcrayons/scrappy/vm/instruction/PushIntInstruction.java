package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class PushIntInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackOverflowException {
		int value = Integer.parseInt(vm.instructionList.nextInstruction());
		vm.stack.pushInt(value);
	}

}
