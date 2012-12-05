package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.util.Utils;

public class NewStringInstruction implements Instruction {

	private static String STRING_CLASS = "String";

	@Override
	public void process(ScrappyVM vm) throws ClassNotFoundException, StackOverflowException {
		String value = vm.instructionList.nextInstruction();
		byte[] bytes = value.getBytes();
		SvmClass clazz = vm.permGenSpace.getClass(STRING_CLASS);
		int pointer = vm.heap.allocByteClass(clazz, bytes);

		Utils.setObjectFieldValue(vm.heap.getSpace(), pointer, 0, Utils.intToByteArray(value.length()));
		Utils.setObjectFieldValue(vm.heap.getSpace(), pointer, 1, Utils.intToByteArray(bytes.length));

		vm.stack.pushPointer(pointer);
	}

}
