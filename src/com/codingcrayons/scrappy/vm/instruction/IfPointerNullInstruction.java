package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class IfPointerNullInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackOverflowException, StackException {
		int offset = Integer.parseInt(vm.instructionList.nextInstruction());
		int pointer = vm.stack.popInt();

		if (pointer != 0) {
			vm.instructionList.jump(vm.instructionList.getPc() + offset);
		}
	}

}
