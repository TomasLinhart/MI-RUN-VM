package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class DivIntInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException, StackException {
		int v1 = vm.stack.popInt();
		int v2 = vm.stack.popInt();
		vm.stack.pushInt(v2 % v1);
	}

}
