package com.codingcrayons.scrappy.vm.permgen;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.*;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.stack.SvmStack;
import com.codingcrayons.scrappy.vm.util.Utils;

import java.util.HashMap;

public class SvmClass {

	/**
	 * permgen address
	 */
	public int address;
	public String name;
	public SvmField[] fields;
	public SvmMethod[] methods;
	public SvmClass superClass;
	public String superClassName;

	public SvmClass(String name, SvmField[] fields, SvmMethod[] methods, String superClassName) {
		this.name = name;
		this.fields = fields;

		this.methods = methods;
		this.superClassName = superClassName;
	}

	public void setSuperClass(SvmClass superClass) {
		this.superClass = superClass;
	}

	public String getSuperClassName() {
		return superClassName;
	}


	public LookupReturn lookup(String methodIdent, ScrappyVM vm) throws StackException, ClassNotFoundException {
		return lookup(getMethodSuperClassName(methodIdent), getMethodName(methodIdent), getMethodArgsCount(methodIdent), vm);
	}

	private LookupReturn lookup(String superClassName, String methodName, int argsCount, ScrappyVM vm) throws StackException, ClassNotFoundException {
		byte[][] args = new byte[argsCount][SvmType.TYPE_BYTE_SIZE];
		String[] currentArgsClassNames = new String[argsCount];
		int bestRate = Integer.MAX_VALUE;
		LookupReturn lr = new LookupReturn();

		for (int i = argsCount - 1; i >= 0; i--) {
			byte[] value = vm.stack.popValue();
			if (Utils.isPointer(value[3])) {
				int pointer = Utils.convertSVMTypeToInt(Utils.byteArrayToInt(value, 0));
				if (pointer == 0) {
					currentArgsClassNames[i]  = "";
				} else {
					currentArgsClassNames[i]  = vm.heap.loadObject(pointer).name;
				}
			} else {
				currentArgsClassNames[i] = "Integer";
			}
			args[i] = value;
		}

		lr.args = args;

		SvmClass svmClass = this;
		while (svmClass != null) {
			// if super class specified, skip to super class with this name
			if (superClassName == null || superClassName.equals(svmClass.name)) {
				superClassName = null; // found, control no more
				for (SvmMethod method : svmClass.methods) {
					if (method.name.equals(methodName) && method.arguments.length == argsCount) {
						int rating = rateMethod(method, currentArgsClassNames, vm);
						if (rating == -1) {
							continue;
						}
						if (rating == 0) {
							lr.method = method;
							return lr;
						}
						if (rating < bestRate) {
							lr.method = method;
						}
					}
				}
			}
			svmClass = svmClass.superClass;
		}

		return lr;
	}

	private static int rateMethod(SvmMethod method, String[] argsNames, ScrappyVM vm) throws ClassNotFoundException {
		int rate = 0;
		for (int i = 0; i < method.arguments.length; i++) {
			String argClassName = method.arguments[i].className;
			if (!argClassName.equals(argsNames[i])) {
				// try superclasses
				if (argClassName.equals("") || argClassName.equals("Any")) {
					continue;
				} else if (argClassName.equals("Integer")) {
					// Integer can not be subclassed
					return -1;
				} else {
					SvmClass clazz = vm.permGenSpace.getClass(argClassName);
					int p = 0;
					boolean found = false;
					while (clazz != null) {
						p++;
						if (clazz.name.equals(argClassName)) {
							found = true;
							break;
						}
						clazz = clazz.superClass;
					}
					if (!found) {
						// argument not passed
						return -1;
					}
					rate += p;
				}
			}
		}
		return rate;
	}

	private String getMethodSuperClassName(String methodIdent) {
		if (methodIdent.startsWith("::")) {
			return null;
		}
		return methodIdent.substring(0, methodIdent.indexOf(":"));
	}

	private String getMethodName(String methodIdent) {
		int start = methodIdent.indexOf(":");
		return methodIdent.substring(start + 2, methodIdent.indexOf(":", start + 2));
	}

	private int getMethodArgsCount(String methodIdent) {
		int start = methodIdent.indexOf(":");
		start =  methodIdent.indexOf(":", start + 2);

		int count = 0;
		try {
			count = Integer.parseInt(methodIdent.substring(start + 2));
		} catch (NumberFormatException e) {
		}
		return count;
	}

}
