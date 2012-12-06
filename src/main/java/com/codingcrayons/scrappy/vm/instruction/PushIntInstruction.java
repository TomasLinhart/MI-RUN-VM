package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class PushIntInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException {
		int value = Integer.parseInt(params[0]);
		vm.stack.pushInt(value);
	}

}
