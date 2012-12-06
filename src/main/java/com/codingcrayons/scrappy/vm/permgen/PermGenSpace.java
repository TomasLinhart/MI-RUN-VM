package com.codingcrayons.scrappy.vm.permgen;

import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentException;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class PermGenSpace {

	private final HashMap<String, Integer> classTable;
	private final SvmClass[] classes;
	private int counter;

	public PermGenSpace(String[] classFiles, InstructionList instructionList) throws DocumentException {
		List<SvmClass> loadedClasses = ClassLoader.load(classFiles, instructionList);

		classTable = new HashMap<String, Integer>(loadedClasses.size());
		classes = new SvmClass[loadedClasses.size()];

		for (SvmClass clazz : loadedClasses) {
			addClass(clazz);
		}
	}

	private void addClass(SvmClass clazz) {
		clazz.address = counter;
		classTable.put(clazz.name, counter);
		classes[counter] = clazz;
		counter++;
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

}
