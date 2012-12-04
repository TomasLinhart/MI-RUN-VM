package com.codingcrayons.scrappy.vm.permgen;

import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class ClassLoader {

	public static SvmClass load(String classFile, InstructionList instructionList) {
		instructionList.addInstruction(null);
		instructionList.addInstruction("push");
		instructionList.addInstruction("R0");

		// new object, call main

		int mainP = instructionList.addInstruction("push");
		instructionList.addInstruction("HELLO");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print
		instructionList.addInstruction("return");

		SvmMethod mainM = new SvmMethod("main:", new SvmField[] {}, new SvmField[] {}, SvmType.VOID, mainP);

		int cP = instructionList.addInstruction("return");
		SvmMethod c = new SvmMethod("_:", new SvmField[] {}, new SvmField[] {}, SvmType.VOID, cP);

		SvmClass mainC = new SvmClass("Main", new SvmMethod[] { c }, new SvmField[] {}, new SvmMethod[] { mainM }, null);

		return mainC;
	}

}
