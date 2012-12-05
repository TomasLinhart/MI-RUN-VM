package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;

public class ReturnInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) {
		int address = vm.stack.discardStackFrame();
		vm.instructionList.jump(address);
	}

}
