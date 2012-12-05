package com.codingcrayons.scrappy.vm.permgen;

import java.util.HashMap;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class PermGenSpace {

	private final HashMap<String, Integer> classTable;
	private final SvmClass[] classes;
	private int counter;

	public PermGenSpace(String[] classFiles, InstructionList instructionList) {
		classTable = new HashMap<String, Integer>(classFiles.length);
		classes = new SvmClass[classFiles.length + 1];
		for (String classFile : classFiles) {
			addClass(ClassLoader.load(classFile, instructionList));
		}
		addClass(init(instructionList));
	}

	private void addClass(SvmClass clazz) {
		clazz.address = counter;
		classTable.put(clazz.name, counter);
		classes[counter] = clazz;
		counter++;

		crateValuePointers(clazz);
	}

	public SvmClass getClass(String name) throws ClassNotFoundException {
		Integer address = classTable.get(name);
		if (address == null) {
			throw new ClassNotFoundException(name);
		}
		return classes[address];
	}

	public SvmClass getClass(int address) {
		return classes[address];
	}

	private void crateValuePointers(SvmClass clazz) {
		int valuePointer = 0;
		for (SvmField field : clazz.fields) {
			field.valuePointer = valuePointer;
			valuePointer += field.type.getSize();
		}
	}

	private SvmClass init(InstructionList instructionList) {

		int length = instructionList.addInstruction("getfield");
		instructionList.addInstruction("0");
		instructionList.addInstruction("ireturn");

		SvmMethod lengthM = new SvmMethod("length:", new SvmField[] {}, new SvmField[] {}, SvmType.VOID, length);

		int cP = instructionList.addInstruction("return");
		SvmMethod c = new SvmMethod("_:", new SvmField[] {}, new SvmField[] {}, SvmType.INT, cP);

		SvmClass mainC = new SvmClass(
				"String",
				new SvmMethod[] { c },
				new SvmField[] { new SvmField("length", SvmType.INT), new SvmField("bytesCount", SvmType.INT) },
				new SvmMethod[] { lengthM },
				null);

		return mainC;
	}
}
