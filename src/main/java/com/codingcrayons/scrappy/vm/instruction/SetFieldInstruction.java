package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SetFieldInstruction implements Instruction {

	@Override
	public void process(ScrappyVM vm) throws ClassNotFoundException, StackOverflowException, StackException, PointerIsNullException {
		int fieldIndex = Integer.parseInt(vm.instructionList.nextInstruction());
		int objPointer = vm.stack.popPointer();

		Utils.checkNullPointer(objPointer);

		byte[] value = vm.stack.popValue();

		Utils.setObjectFieldValue(vm.heap.getSpace(), objPointer, fieldIndex, value);
	}

}
