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
		if (identifier.equals("ipush")) {
			return instructions[0];
		}
		if (identifier.equals("ppush")) {
			return instructions[1];
		}
		if (identifier.equals("syscall")) {
			return instructions[2];
		}
		if (identifier.equals("new")) {
			return instructions[3];
		}
		if (identifier.equals("invokevirtual")) {
			return instructions[4];
		}
		if (identifier.equals("getfield")) {
			return instructions[5];
		}
		if (identifier.equals("setfield")) {
			return instructions[6];
		}
		if (identifier.equals("ireturn")) {
			return instructions[7];
		}
		if (identifier.equals("preturn")) {
			return instructions[8];
		}
		if (identifier.equals("return")) {
			return instructions[9];
		}
		if (identifier.equals("iadd")) {
			return instructions[10];
		}
		if (identifier.equals("isub")) {
			return instructions[11];
		}
		if (identifier.equals("imul")) {
			return instructions[12];
		}
		if (identifier.equals("idiv")) {
			return instructions[13];
		}
		if (identifier.equals("imod")) {
			return instructions[14];
		}
		if (identifier.equals("ifeq")) {
			return instructions[15];
		}
		if (identifier.equals("ifge")) {
			return instructions[16];
		}
		if (identifier.equals("ifgt")) {
			return instructions[17];
		}
		if (identifier.equals("ifle")) {
			return instructions[18];
		}
		if (identifier.equals("iflt")) {
			return instructions[19];
		}
		if (identifier.equals("ifneq")) {
			return instructions[20];
		}
		if (identifier.equals("ifnotnull")) {
			return instructions[21];
		}
		if (identifier.equals("ifnull")) {
			return instructions[22];
		}
		if (identifier.equals("newarray")) {
			return instructions[23];
		}
		if (identifier.equals("iload")) {
			return instructions[24];
		}
		if (identifier.equals("pload")) {
			return instructions[25];
		}
		if (identifier.equals("istore")) {
			return instructions[26];
		}
		if (identifier.equals("pstore")) {
			return instructions[27];
		}
		if (identifier.equals("jump")) {
			return instructions[28];
		}
		if (identifier.equals("arraylength")) {
			return instructions[29];
		}
		if (identifier.equals("newstring")) {
			return instructions[30];
		}
		if (identifier.equals("dup")) {
			return instructions[31];
		}

		throw new UnknownInstructionException(identifier);
	}

}
