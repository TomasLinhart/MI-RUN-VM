package com.codingcrayons.scrappy.vm.permgen;

public class SvmField {

	public String name;
	public SvmType type;
	int valuePointer;

	public SvmField(String name, SvmType type) {
		this.name = name;
		this.type = type;
	}
}
