package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class StoreInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, StackException, PointerIsNullException {
		int localFieldIndex = Integer.parseInt(params[0]);
		byte[] value = vm.stack.popValue();

		vm.stack.setLocalValue(localFieldIndex, value);
	}

}
