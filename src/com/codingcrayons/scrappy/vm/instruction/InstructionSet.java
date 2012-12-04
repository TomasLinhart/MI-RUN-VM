package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.exceptions.UnknownInstructionException;

public class InstructionSet {

	public static Instruction[] instructions;

	static {
		instructions = new Instruction[3];
		instructions[0] = new PushIntInstruction();
		instructions[1] = new SyscallInstruction();
		instructions[2] = new ReturnInstruction();
	}

	public static Instruction getInstruction(String identifier) throws UnknownInstructionException {
		if (identifier.equals("push")) {
			return instructions[0];
		}
		if (identifier.equals("syscall")) {
			return instructions[1];
		}
		if (identifier.equals("return")) {
			return instructions[2];
		}
		throw new UnknownInstructionException(identifier);
	}

}
