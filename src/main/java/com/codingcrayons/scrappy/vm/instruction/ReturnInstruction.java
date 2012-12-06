package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;

public class ReturnInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) {
		int address = vm.stack.discardStackFrame();
		vm.instructionList.jump(address);
	}

}
