package com.codingcrayons.scrappy.vm;

import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.instruction.InstructionSet;

public class Interpreter {

	public static void interpret(ScrappyVM vm) throws ScrappyVmException {
		String instruction = null;
		while ((instruction = vm.instructionList.nextInstruction()) != null) {
			InstructionSet.getInstruction(instruction).process(vm);
		}
	}

}
