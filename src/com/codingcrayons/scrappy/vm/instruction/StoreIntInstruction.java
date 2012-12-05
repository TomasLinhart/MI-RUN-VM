package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class StoreIntInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws ClassNotFoundException, StackOverflowException, StackException, PointerIsNullException {
		int localFieldIndex = Integer.parseInt(vm.instructionList.nextInstruction());
		int value = vm.stack.popInt();

		vm.stack.setLocalInt(localFieldIndex, value);
	}

}
