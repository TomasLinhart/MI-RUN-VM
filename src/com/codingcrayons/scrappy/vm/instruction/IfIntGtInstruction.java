package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class IfIntGtInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws StackOverflowException, StackException {
		int offset = Integer.parseInt(vm.instructionList.nextInstruction());
		int v1 = vm.stack.popInt();
		int v2 = vm.stack.popInt();

		if (v2 <= v1) {
			vm.instructionList.jump(vm.instructionList.getPc() + offset);
		}
	}

}
