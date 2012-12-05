package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.syscalls.SyscallSet;

public class SyscallInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws ScrappyVmException {
		int index = Integer.parseInt(vm.instructionList.nextInstruction());
		SyscallSet.get(index).call(vm);
	}

}
