package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class IfIntGtInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException, StackException {
		int offset = Integer.parseInt(params[0]);
		int v1 = vm.stack.popInt();
		int v2 = vm.stack.popInt();

		if (v2 <= v1) {
			// -1: return to if
			vm.instructionList.jump(vm.instructionList.getPc() + offset - 1);
		}
	}

}
