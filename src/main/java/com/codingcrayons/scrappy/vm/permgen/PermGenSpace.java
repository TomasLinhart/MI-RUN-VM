package com.codingcrayons.scrappy.vm.permgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentException;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class PermGenSpace {

	private final HashMap<String, Integer> classTable;
	private final ArrayList<SvmClass> classes;
	private int counter;

	public PermGenSpace(String[] classFiles, InstructionList instructionList) throws DocumentException {
		// load libraries
		String[] libs = new String[] { "classes/Array.xml", "classes/FileReader.xml", "classes/FileWriter.xml", "classes/String.xml"};
		List<SvmClass> loadedClasses = ClassLoader.load( libs, instructionList);
		loadedClasses.addAll(ClassLoader.load(classFiles, instructionList));

		classTable = new HashMap<String, Integer>(loadedClasses.size());
		classes = new ArrayList<SvmClass>(loadedClasses.size() + 5);

		for (SvmClass clazz : loadedClasses) {
			addClass(clazz);
		}
	}

	public void addClass(SvmClass clazz) {
		clazz.address = counter;
		classTable.put(clazz.name, counter);
		classes.add(clazz);
		counter++;
	}

	public SvmClass getClass(String name) throws ClassNotFoundException {
		Integer address = classTable.get(name);
		if (address == null) {
			throw new ClassNotFoundException(name);
		}
		return classes.get(address);
	}

	public SvmClass getClass(int address) {
		return classes.get(address);
	}

}
