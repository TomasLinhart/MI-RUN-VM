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
		classes = new SvmClass[classFiles.length];
		for (String classFile : classFiles) {
			addClass(ClassLoader.load(classFile, instructionList));
		}
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

}
