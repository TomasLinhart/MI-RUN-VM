package com.codingcrayons.scrappy.vm;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SvmHeap {

	private static final Logger logger = Logger.getLogger(SvmHeap.class);

	public static final int OBJECT_HEADER_BYTES = 8;

	private final byte[] space;
	private int next;
	private final ScrappyVM vm;

	public SvmHeap(int size, ScrappyVM vm) {
		logger.info("Heap of " + size + " bytes ready");

		space = new byte[size];
		next = 0;
		space[next++] = 0; // null pointer location
		this.vm = vm;
	}

	public int alloc(SvmClass clazz) {
		int start = next;

		insertIntToHeap(clazz.address);
		insertIntToHeap(0); // array size not set
		int fieldsSize = clazz.fields.length * SvmType.STORED_TYPE_BYTE_SIZE;
		// initialize fields to default value
		for (; fieldsSize > 0; fieldsSize--) {
			space[next++] = 0;
		}
		return start;
	}

	public int allocArray(int size) throws ClassNotFoundException {
		int start = next;

		insertIntToHeap(vm.permGenSpace.getClass("Array").address);
		insertIntToHeap(size); // array size

		int fieldsSize = size * SvmType.STORED_TYPE_BYTE_SIZE;
		// initialize fields to default value
		for (; fieldsSize > 0; fieldsSize--) {
			space[next++] = 0;
		}
		return start;
	}

	public int allocByteClass(SvmClass clazz, byte[] bytes) {
		int start = alloc(clazz);

		for (int i = 0; i < bytes.length; i++) {
			space[next++] = bytes[i];
		}
		return start;
	}

	public SvmClass loadObject(int address) {
		SvmClass clazz = vm.permGenSpace.getClass(Utils.byteArrayToInt(space, address));
		return clazz;
	}

	private void insertIntToHeap(int value) {
		byte[] intBytes = Utils.intToByteArray(value);
		space[next++] = intBytes[0];
		space[next++] = intBytes[1];
		space[next++] = intBytes[2];
		space[next++] = intBytes[3];
	}

	public byte[] getSpace() {
		return space;
	}

}
