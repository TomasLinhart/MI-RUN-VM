package com.codingcrayons.scrappy.vm.permgen;

public class SvmMethod {

	public String name;
	public SvmField[] arguments;
	public SvmField[] locals;
	public SvmType returnType;
	public int instructionPointer;

	public SvmMethod(String name, SvmField[] arguments, SvmField[] locals, SvmType returnType, int instructionPointer) {
		this.name = name;
		this.arguments = arguments;
		this.locals = locals;
		this.returnType = returnType;
		this.instructionPointer = instructionPointer;
	}

}
