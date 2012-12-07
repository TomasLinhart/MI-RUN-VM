package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class GetFieldInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, StackException, PointerIsNullException {
		int objPointer = vm.stack.popPointer();

		int fieldIndex;
		if (params.length > 0) {
			fieldIndex = Integer.parseInt(params[0]);
		} else {
			fieldIndex = vm.stack.popInt();
		}

		Utils.checkNullPointer(objPointer);

		byte[] value = Utils.getObjectFieldValue(vm.heap.getSpace(), objPointer, fieldIndex);
		vm.stack.push(value);
	}

}
