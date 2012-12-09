package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class NewArrayInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, StackException, HeapOutOfMemoryException {
		int size;
		if (params.length > 0) {
			size = Integer.parseInt(params[0]);
		} else {
			size = vm.stack.popInt();
		}

		int pointer = vm.heap.allocArray(size);
		vm.stack.pushPointer(pointer);
	}

}
