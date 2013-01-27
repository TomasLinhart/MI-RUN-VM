package com.codingcrayons.scrappy.vm.permgen;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.codingcrayons.scrappy.vm.instruction.InstructionList;

public class ClassLoader {

	public static List<SvmClass> load(String[] classFiles, InstructionList instructionList) throws DocumentException {

		List<SvmClass> loadedClasses = new LinkedList<SvmClass>();
		for (String classFile : classFiles) {
			loadedClasses.addAll(parse(new File(classFile), instructionList));
		}

		return loadedClasses;
	}

	private static List<SvmClass> parse(File file, InstructionList instructionList) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);

		Element root = document.getRootElement();

		if (root.getName().equals("classes")) {
			@SuppressWarnings("rawtypes")
			Iterator iterator = root.elementIterator();

			List<SvmClass> loadedClasses = new ArrayList<SvmClass>(20);
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				loadedClasses.add(processClassElement(element, instructionList));
			}
			return loadedClasses;
		} else if (root.getName().equals("class")) {
			List<SvmClass> loadedClasses = new ArrayList<SvmClass>(1);
			loadedClasses.add(processClassElement(root, instructionList));
			return loadedClasses;
		}
		return null;
	}

	private static SvmClass processClassElement(Element root, InstructionList instructionList) {

		List<SvmField> fields = new ArrayList<SvmField>(0);
		List<SvmMethod> methods = new LinkedList<SvmMethod>();

		@SuppressWarnings("rawtypes")
		Iterator iterator = root.elementIterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			if (element.getName().equals("fields")) {
				fields = processFieldsElement(element);
			} else if (element.getName().equals("methods")) {
				@SuppressWarnings("rawtypes")
				Iterator methodsIterator = element.elementIterator();
				while (methodsIterator.hasNext()) {
					Element methodElement = (Element) methodsIterator.next();

					List<SvmField> arguments = new ArrayList<SvmField>(0);
					List<SvmField> locals = new ArrayList<SvmField>(0);
					int instructionPointer = -1;

					@SuppressWarnings("rawtypes")
					Iterator methodIterator = methodElement.elementIterator();
					while (methodIterator.hasNext()) {
						Element methodSubElement = (Element) methodIterator.next();
						if (methodSubElement.getName().equals("args")) {
							arguments = processFieldsElement(methodSubElement);
						} else if (methodSubElement.getName().equals("locals")) {
							locals = processFieldsElement(methodSubElement);
						} else if (methodSubElement.getName().equals("instructions")) {
							@SuppressWarnings("rawtypes")
							Iterator instructionsIterator = methodSubElement.elementIterator();
							while (instructionsIterator.hasNext()) {
								Element instruction = (Element) instructionsIterator.next();
								int position = instructionList.addInstruction(instruction.getText());
								if (instructionPointer < 0) {
									instructionPointer = position;
								}
							}
						}
					}

					String name = "";
					SvmType returnType = null;
					@SuppressWarnings("rawtypes")
					Iterator methodAttributesIterator = methodElement.attributeIterator();
					while (methodAttributesIterator.hasNext()) {
						Attribute attribute = (Attribute) methodAttributesIterator.next();
						if (attribute.getName().equals("name")) {
							name = attribute.getValue();
						} else if (attribute.getName().equals("type")) {
							if (attribute.getValue().equals("Integer")) {
								returnType = SvmType.INT;
							} else if (attribute.getValue().equals("void")) {
								returnType = SvmType.VOID;
							} else {
								returnType = SvmType.POINTER;
							}
						}
					}
					SvmMethod m = new SvmMethod(
							name,
							arguments.toArray(new SvmField[arguments.size()]),
							locals.toArray(new SvmField[locals.size()]),
							returnType,
							instructionPointer);
					methods.add(m);
				}
			}
		}
		String name = null;
		String parentName = null;
		@SuppressWarnings("rawtypes")
		Iterator classAttributesIterator = root.attributeIterator();
		while (classAttributesIterator.hasNext()) {
			Attribute attribute = (Attribute) classAttributesIterator.next();
			if (attribute.getName().equals("name")) {
				name = attribute.getValue();
			}
			if (attribute.getName().equals("parentName")) {
				parentName = attribute.getValue();
			}
		}
		return new SvmClass(name, fields.toArray(new SvmField[fields.size()]), methods.toArray(new SvmMethod[methods.size()]), parentName);
	}

	private static List<SvmField> processFieldsElement(Element fieldsElement) {
		List<SvmField> fields = new LinkedList<SvmField>();

		@SuppressWarnings("rawtypes")
		Iterator fieldsIterator = fieldsElement.elementIterator();
		while (fieldsIterator.hasNext()) {
			Element fieldElement = (Element) fieldsIterator.next();

			String name = "";
			SvmType type = null;
			String className = "";
			@SuppressWarnings("rawtypes")
			Iterator fieldAttributesIterator = fieldElement.attributeIterator();
			while (fieldAttributesIterator.hasNext()) {
				Attribute attribute = (Attribute) fieldAttributesIterator.next();
				if (attribute.getName().equals("name")) {
					name = attribute.getValue();
				} else if (attribute.getName().equals("type")) {
					if (attribute.getValue().equals("Integer")) {
						type = SvmType.INT;
					} else {
						type = SvmType.POINTER;
					}
					className = attribute.getValue();
				}
			}
			fields.add(new SvmField(name, type, className));
		}
		return fields;
	}

}
