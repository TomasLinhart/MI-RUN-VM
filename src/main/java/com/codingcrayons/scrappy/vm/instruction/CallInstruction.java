package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.MethodNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmMethod;
import com.codingcrayons.scrappy.vm.util.Utils;

public class CallInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws MethodNotFoundException, PointerIsNullException, StackException, StackOverflowException {
		String methodName = params[0];
		int objPointer = vm.stack.popPointer();

		Utils.checkNullPointer(objPointer);

		SvmClass clazz = vm.heap.loadObject(objPointer);
		SvmMethod method = clazz.lookup(methodName);

		if (method == null) {
			throw new MethodNotFoundException(clazz.name, methodName);
		}

		vm.stack.beginStackFrame(vm.instructionList.getPc() + 1, objPointer, method);

		vm.instructionList.jump(method.instructionPointer);
	}

}
