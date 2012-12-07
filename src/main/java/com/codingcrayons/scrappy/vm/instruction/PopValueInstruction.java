package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;

public class PopValueInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackException {
		vm.stack.popValue();
	}

}
