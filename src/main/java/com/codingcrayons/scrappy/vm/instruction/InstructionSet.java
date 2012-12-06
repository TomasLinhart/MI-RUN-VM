package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.exceptions.UnknownInstructionException;

public class InstructionSet {

	public static Instruction[] instructions;

	static {
		instructions = new Instruction[32];
		instructions[0] = new PushIntInstruction();
		instructions[1] = new PushPointerInstruction();
		instructions[2] = new SyscallInstruction();
		instructions[3] = new NewInstruction();
		instructions[4] = new CallInstruction();
		instructions[5] = new GetFieldInstruction();
		instructions[6] = new SetFieldInstruction();
		instructions[7] = new ReturnIntInstruction();
		instructions[8] = new ReturnPointerInstruction();
		instructions[9] = new ReturnInstruction();
		instructions[10] = new AddIntInstruction();
		instructions[11] = new SubIntInstruction();
		instructions[12] = new MulIntInstruction();
		instructions[13] = new DivIntInstruction();
		instructions[14] = new ModIntInstruction();
		instructions[15] = new IfIntEqInstruction();
		instructions[16] = new IfIntGeInstruction();
		instructions[17] = new IfIntGtInstruction();
		instructions[18] = new IfIntLeInstruction();
		instructions[19] = new IfIntLtInstruction();
		instructions[20] = new IfIntNeqInstruction();
		instructions[21] = new IfPointerNotNullInstruction();
		instructions[22] = new IfPointerNullInstruction();
		instructions[23] = new NewArrayInstruction();
		instructions[24] = new LoadIntInstruction();
		instructions[25] = new LoadPointerInstruction();
		instructions[26] = new StoreIntInstruction();
		instructions[27] = new StorePointerInstruction();
		instructions[28] = new JumpInstruction();
		instructions[29] = new ArrayLengthInstruction();
		instructions[30] = new NewStringInstruction();
		instructions[31] = new DupInstruction();
	}

	public static Instruction getInstruction(String identifier) throws UnknownInstructionException {
		if (identifier.startsWith("ipush")) {
			return instructions[0];
		}
		if (identifier.startsWith("ppush")) {
			return instructions[1];
		}
		if (identifier.startsWith("syscall")) {
			return instructions[2];
		}
		if (identifier.startsWith("invokevirtual")) {
			return instructions[4];
		}
		if (identifier.startsWith("getfield")) {
			return instructions[5];
		}
		if (identifier.startsWith("setfield")) {
			return instructions[6];
		}
		if (identifier.startsWith("ireturn")) {
			return instructions[7];
		}
		if (identifier.startsWith("preturn")) {
			return instructions[8];
		}
		if (identifier.startsWith("return")) {
			return instructions[9];
		}
		if (identifier.startsWith("iadd")) {
			return instructions[10];
		}
		if (identifier.startsWith("isub")) {
			return instructions[11];
		}
		if (identifier.startsWith("imul")) {
			return instructions[12];
		}
		if (identifier.startsWith("idiv")) {
			return instructions[13];
		}
		if (identifier.startsWith("imod")) {
			return instructions[14];
		}
		if (identifier.startsWith("ifeq")) {
			return instructions[15];
		}
		if (identifier.startsWith("ifge")) {
			return instructions[16];
		}
		if (identifier.startsWith("ifgt")) {
			return instructions[17];
		}
		if (identifier.startsWith("ifle")) {
			return instructions[18];
		}
		if (identifier.startsWith("iflt")) {
			return instructions[19];
		}
		if (identifier.startsWith("ifneq")) {
			return instructions[20];
		}
		if (identifier.startsWith("ifnotnull")) {
			return instructions[21];
		}
		if (identifier.startsWith("ifnull")) {
			return instructions[22];
		}
		if (identifier.startsWith("newarray")) {
			return instructions[23];
		}
		if (identifier.startsWith("iload")) {
			return instructions[24];
		}
		if (identifier.startsWith("pload")) {
			return instructions[25];
		}
		if (identifier.startsWith("istore")) {
			return instructions[26];
		}
		if (identifier.startsWith("pstore")) {
			return instructions[27];
		}
		if (identifier.startsWith("jump")) {
			return instructions[28];
		}
		if (identifier.startsWith("arraylength")) {
			return instructions[29];
		}
		if (identifier.startsWith("newstring")) {
			return instructions[30];
		}
		if (identifier.startsWith("dup")) {
			return instructions[31];
		}
		if (identifier.startsWith("new")) {
			return instructions[3];
		}

		throw new UnknownInstructionException(identifier);
	}

}
