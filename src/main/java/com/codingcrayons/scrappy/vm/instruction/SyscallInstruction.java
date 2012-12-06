package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.syscalls.SyscallSet;

public class SyscallInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ScrappyVmException {
		int index = Integer.parseInt(params[0]);
		SyscallSet.get(index).call(vm);
	}

}
