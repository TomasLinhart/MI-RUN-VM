package com.codingcrayons.scrappy.vm.permgen;

import java.util.HashMap;

public class SvmClass {

	/**
	 * permgen address
	 */
	public int address;
	public String name;
	public SvmField[] fields;
	public HashMap<String, SvmMethod> methods;
	public SvmClass superClass;

	public SvmClass(String name, SvmField[] fields, SvmMethod[] methods, SvmClass superClass) {
		this.name = name;
		this.fields = fields;

		this.methods = new HashMap<String, SvmMethod>(methods.length);
		for (SvmMethod method : methods) {
			this.methods.put(method.name, method);
		}

		this.superClass = superClass;
	}

	public SvmMethod lookup(String methodName) {
		SvmMethod method = methods.get(methodName);
		if (method == null && superClass != null) {
			method = superClass.lookup(methodName);
		}
		return method;
	}

}
