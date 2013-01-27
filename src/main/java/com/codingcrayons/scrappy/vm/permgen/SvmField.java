package com.codingcrayons.scrappy.vm.permgen;

public class SvmField {

	public String name;
	public SvmType type;
	public String className;

	public SvmField(String name, SvmType type, String className) {
		this.name = name;
		this.type = type;
		this.className = className;
	}
}
