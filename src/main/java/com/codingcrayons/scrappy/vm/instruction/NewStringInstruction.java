package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.util.Utils;

public class NewStringInstruction extends Instruction {

	private static String STRING_CLASS = "String";

	@Override
	public void process(ScrappyVM vm, String[] params) throws ClassNotFoundException, StackOverflowException, HeapOutOfMemoryException {
		String value = params[0];
		byte[] bytes = value.getBytes();
		SvmClass clazz = vm.permGenSpace.getClass(STRING_CLASS);

		vm.stack.pushPointer(Utils.createSringOnHeap(vm, clazz, bytes, value));
	}
}
