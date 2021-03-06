package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class JumpInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException, StackException {
		int offset = Integer.parseInt(params[0]);
		vm.instructionList.jump(vm.instructionList.getPc() + offset - 1);
	}

}
