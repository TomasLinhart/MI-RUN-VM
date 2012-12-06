package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class DupInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException, StackException {
		byte[] bytes = vm.stack.popValue();
		vm.stack.push(bytes);
		vm.stack.push(bytes);
	}

}
