package com.codingcrayons.scrappy.vm.instruction;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.*;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.permgen.LookupReturn;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmMethod;
import com.codingcrayons.scrappy.vm.util.Utils;

public class InvokevirtualInstruction extends Instruction {

	private static final Logger logger = Logger.getLogger(InvokevirtualInstruction.class);

	@Override
	public void process(ScrappyVM vm, String[] params) throws MethodNotFoundException, PointerIsNullException, StackException, StackOverflowException, ClassNotFoundException {
		String methodName = params[0];
		int objPointer = vm.stack.popPointer();

		logger.debug("invoked " + methodName + vm.instructionList.getPc());

		Utils.checkNullPointer(objPointer);

		SvmClass clazz = vm.heap.loadObject(objPointer);
		LookupReturn lr = clazz.lookup(methodName, vm);

		if (lr.method == null) {
			throw new MethodNotFoundException(clazz.name, methodName);
		}

		// pc is set to next instruction
		vm.stack.beginStackFrame(vm.instructionList.getPc(), objPointer, lr);

		vm.instructionList.jump(lr.method.instructionPointer);
	}

}
