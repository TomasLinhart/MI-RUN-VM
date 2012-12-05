package com.codingcrayons.scrappy.vm.permgen;

import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class ClassLoader {

	public static SvmClass load(String classFile, InstructionList instructionList) {
		// new object, call main

		int mainP = instructionList.addInstruction("ipush 101222");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("newarray");
		instructionList.addInstruction("5");
		instructionList.addInstruction("pstore");
		instructionList.addInstruction("1");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("arraylength");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("ipush");
		instructionList.addInstruction("10");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("setfield");
		instructionList.addInstruction("0");
		instructionList.addInstruction("ipush");
		instructionList.addInstruction("9");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("setfield");
		instructionList.addInstruction("1");
		instructionList.addInstruction("ipush");
		instructionList.addInstruction("8");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("setfield");
		instructionList.addInstruction("2");
		instructionList.addInstruction("ipush");
		instructionList.addInstruction("7");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("setfield");
		instructionList.addInstruction("3");
		instructionList.addInstruction("ipush");
		instructionList.addInstruction("6");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("setfield");
		instructionList.addInstruction("4");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("0");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("1");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("2");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("4");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("3");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0"); // print int

		instructionList.addInstruction("newstring");
		instructionList.addInstruction("HELLO FROM STRING");
		instructionList.addInstruction("pstore");
		instructionList.addInstruction("3");
		// instructionList.addInstruction("invokevirtual");
		// instructionList.addInstruction("length:");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("3");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1"); // print int

		instructionList.addInstruction("newstring");
		instructionList.addInstruction(" SECONF PART");
		instructionList.addInstruction("pstore");
		instructionList.addInstruction("2");
		// instructionList.addInstruction("invokevirtual");
		// instructionList.addInstruction("length:");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("2");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1"); // print int

		instructionList.addInstruction("pload");
		instructionList.addInstruction("2");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("3");
		instructionList.addInstruction("invokevirtual");
		instructionList.addInstruction("append:Pointer");
		instructionList.addInstruction("pstore");
		instructionList.addInstruction("2");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("2");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1"); // print int

		instructionList.addInstruction("ipush");
		instructionList.addInstruction("76598");
		instructionList.addInstruction("pload");
		instructionList.addInstruction("2");
		instructionList.addInstruction("invokevirtual");
		instructionList.addInstruction("append:Int");

		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1"); // print int

		instructionList.addInstruction("newstring");
		instructionList.addInstruction("A H O J");
		instructionList.addInstruction("pstore");
		instructionList.addInstruction("2");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("2");
		instructionList.addInstruction("invokevirtual");
		instructionList.addInstruction("split");

		instructionList.addInstruction("pstore");
		instructionList.addInstruction("1");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("0");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("1");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1");

		instructionList.addInstruction("pload");
		instructionList.addInstruction("1");
		instructionList.addInstruction("getfield");
		instructionList.addInstruction("2");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("1");

		instructionList.addInstruction("newstring");
		instructionList.addInstruction("611");
		instructionList.addInstruction("dup");
		instructionList.addInstruction("invokevirtual");
		instructionList.addInstruction("toInt");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0");
		instructionList.addInstruction("invokevirtual");
		instructionList.addInstruction("toInt");
		instructionList.addInstruction("syscall");
		instructionList.addInstruction("0");

		instructionList.addInstruction("return");

		SvmMethod mainM = new SvmMethod("main:", new SvmField[] {}, new SvmField[] { new SvmField("arr", SvmType.POINTER),
				new SvmField("strA", SvmType.POINTER), new SvmField("strB", SvmType.POINTER) }, SvmType.VOID, mainP);

		int cP = instructionList.addInstruction("return");
		SvmMethod c = new SvmMethod("_:", new SvmField[] {}, new SvmField[] {}, SvmType.VOID, cP);

		SvmClass mainC = new SvmClass("Main", new SvmMethod[] { c }, new SvmField[] {}, new SvmMethod[] { mainM }, null);

		return mainC;
	}

}
