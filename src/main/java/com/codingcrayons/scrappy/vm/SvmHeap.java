package com.codingcrayons.scrappy.vm;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmField;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.stack.SvmStack.StackFrame;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SvmHeap {

	private static final Logger logger = Logger.getLogger(SvmHeap.class);

	public static final int OBJECT_HEADER_BYTES = 8;

	private final byte[] spaceA;
	private final byte[] spaceB;
	private final int activeSpace;
	private int next;
	private final ScrappyVM vm;

	public SvmHeap(int size, ScrappyVM vm) {
		logger.info("Heap of " + size + " bytes ready");
		spaceA = new byte[size / 2];
		spaceB = new byte[size / 2];
		activeSpace = 0;
		next = 0;
		getSpace()[next++] = 0; // null pointer
		this.vm = vm;
	}

	public int alloc(SvmClass clazz) throws HeapOutOfMemoryException {
		return allocOnHeap(clazz, 0, clazz.fields.length * SvmType.STORED_TYPE_BYTE_SIZE);
	}

	public int allocArray(int size) throws ClassNotFoundException, HeapOutOfMemoryException {
		return allocOnHeap(vm.permGenSpace.getClass("Array"), size, size * SvmType.STORED_TYPE_BYTE_SIZE);
	}

	public int allocByteClass(SvmClass clazz, byte[] bytes) throws HeapOutOfMemoryException {
		int fieldsL = clazz.fields.length * SvmType.STORED_TYPE_BYTE_SIZE;
		int start = allocOnHeap(clazz, 0, fieldsL + bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			getSpace()[start + OBJECT_HEADER_BYTES + i + fieldsL] = bytes[i];
		}
		return start;
	}

	private int allocOnHeap(SvmClass clazz, int size, int bytes) throws HeapOutOfMemoryException {
		if (!isEnoughSpace(bytes)) {
			logger.debug("START GC");
		}

		if (!isEnoughSpace(bytes)) {
			throw new HeapOutOfMemoryException();
		}

		int start = next;
		insertIntToHeap(clazz.address);
		insertIntToHeap(size);
		for (; bytes > 0; bytes--) {
			getSpace()[next++] = 0;
		}
		return start;
	}

	private boolean isEnoughSpace(int bytes) {
		return (getSpace().length - next) >= (OBJECT_HEADER_BYTES + bytes);
	}

	public SvmClass loadObject(int address) {
		SvmClass clazz = vm.permGenSpace.getClass(Utils.byteArrayToInt(getSpace(), address));
		return clazz;
	}

	private void insertIntToHeap(int value) {
		byte[] intBytes = Utils.intToByteArray(value);
		getSpace()[next++] = intBytes[0];
		getSpace()[next++] = intBytes[1];
		getSpace()[next++] = intBytes[2];
		getSpace()[next++] = intBytes[3];
	}

	public byte[] getSpace() {
		if (activeSpace == 0) {
			return spaceA;
		}
		return spaceB;
	}

	public int getNext() {
		return next;
	}

	private void collect() {
		byte[] old = getSpace();
		byte[] current = activeSpace == 0 ? spaceB : spaceA;
		Map<Integer, Integer> remap = new TreeMap<Integer, Integer>();
		int currentNext = 0;
		int endPointer = 0;
		Queue<Integer> pointerQueue = new LinkedList<Integer>();

		// find all pointer form stack
		for (StackFrame sf : vm.stack.getStackFrames()) {
			if (sf == null) {
				break;
			}
			for (int i = SvmType.POINTER.getSize(); i < sf.getNext(); i += SvmType.STORED_TYPE_BYTE_SIZE) {
				if (sf.getStackSpace()[i] == SvmType.POINTER.getIdentByte()) {
					pointerQueue.add(Utils.byteArrayToInt(old, i - SvmType.POINTER.getSize()));
				}
			}
		}

		Integer pointer = pointerQueue.poll();
		while (pointer != null) {
			if (remap.containsKey(pointer)) {
				// object already processed
				continue;
			}

			remap.put(pointer, currentNext);
			SvmClass clazz = vm.permGenSpace.getClass(Utils.byteArrayToInt(old, pointer));
			int objectSize = 5;
			endPointer += objectSize;

			// TODO prekopirovat header

			int s = pointer + SvmHeap.OBJECT_HEADER_BYTES;
			for (SvmField field : clazz.fields) {
				if (field.type.equals(SvmType.POINTER)) {
					int fieldPointer = Utils.byteArrayToInt(current, s);
					pointerQueue.add(fieldPointer);

					// zjistit novou hodnotu pointeru (kde bude umisten)
					SvmClass fieldClass = vm.permGenSpace.getClass(Utils.byteArrayToInt(old, fieldPointer));
					int fieldObjectSize = 5;

					// copy pointer with value of endPointer;

					endPointer += fieldObjectSize;
					s += SvmType.STORED_TYPE_BYTE_SIZE;
				} else {
					// just copy
				}
			}

		}

	}

}
