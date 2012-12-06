package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class ArrayLengthInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, StackException {
		int pointer = vm.stack.popPointer();
		int size = Utils.byteArrayToInt(vm.heap.getSpace(), pointer);
		vm.stack.pushInt(size);
	}

}
