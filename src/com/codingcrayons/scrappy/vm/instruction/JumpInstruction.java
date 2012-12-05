package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class JumpInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackOverflowException, StackException {
		int offset = Integer.parseInt(vm.instructionList.nextInstruction());
		vm.instructionList.jump(vm.instructionList.getPc() + offset);
	}

}
