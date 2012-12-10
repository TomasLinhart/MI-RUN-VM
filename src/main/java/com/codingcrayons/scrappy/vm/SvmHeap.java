package com.codingcrayons.scrappy.vm;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.stack.SvmStack.StackFrame;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SvmHeap {

	private static final Logger logger = Logger.getLogger(SvmHeap.class);

	public static final int OBJECT_HEADER_BYTES = 8;

	private final byte[] spaceA;
	private final byte[] spaceB;
	private int activeSpace;
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
			collect();
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

	private int countObjectSize(SvmClass clazz, int pointer) {
		if (clazz.name.equals("Array")) {
			int size = Utils.byteArrayToInt(getSpace(), pointer + SvmType.INT.getSize());
			return OBJECT_HEADER_BYTES + size * SvmType.STORED_TYPE_BYTE_SIZE;
		} else if (clazz.name.equals("String")) {
			int size = Utils.byteArrayToInt(Utils.getObjectFieldValue(getSpace(), pointer, 1), 0);
			return OBJECT_HEADER_BYTES + clazz.fields.length * SvmType.STORED_TYPE_BYTE_SIZE + size;
		} else {
			return OBJECT_HEADER_BYTES + clazz.fields.length * SvmType.STORED_TYPE_BYTE_SIZE;
		}
	}

	private void copyToNewSpace(byte[] from, byte[] to, int toNext, int fromIndex, int size) {
		for (int i = 0; i < size; i++) {
			to[toNext++] = from[fromIndex + i];
		}
	}

	private void collect() {
		byte[] current = activeSpace == 0 ? spaceB : spaceA;
		Map<Integer, Integer> remap = new TreeMap<Integer, Integer>();
		int currentNext = 0;
		int endPointer = 1;
		Queue<Integer> pointerQueue = new LinkedList<Integer>();
		current[currentNext++] = 0; // null pointer

		// find all pointer form stack
		for (StackFrame sf : vm.stack.getStackFrames()) {
			if (sf == null) {
				break;
			}
			for (int i = SvmType.POINTER.getSize(); i < sf.getNext(); i += SvmType.STORED_TYPE_BYTE_SIZE) {
				if (sf.getStackSpace()[i] == SvmType.POINTER.getIdentByte()) {
					int pointer = Utils.byteArrayToInt(sf.getStackSpace(), i - SvmType.POINTER.getSize());

					if (pointer == 0) {
						// null pointer
						continue;
					}

					Integer map = remap.get(pointer);
					if (map != null) {
						// replace stack pointer to new value and continue
						copyToNewSpace(Utils.intToByteArray(map), sf.getStackSpace(), i - SvmType.POINTER.getSize(), 0, SvmType.TYPE_BYTE_SIZE);
					} else {
						remap.put(pointer, currentNext);
						copyToNewSpace(Utils.intToByteArray(currentNext), sf.getStackSpace(), i - SvmType.POINTER.getSize(), 0, SvmType.TYPE_BYTE_SIZE);

						endPointer += countObjectSize(vm.permGenSpace.getClass(Utils.byteArrayToInt(getSpace(), pointer)), pointer); //will be copied
						pointerQueue.add(pointer);
						Integer processPointer = null;
						while ((processPointer = pointerQueue.poll()) != null) {
							if (processPointer == 0) {
								// null pointer
								continue;
							}

							SvmClass clazz = vm.permGenSpace.getClass(Utils.byteArrayToInt(getSpace(), processPointer));

							if (clazz.name.equals("String")) {
								// copy string
								copyToNewSpace(getSpace(), current, currentNext, processPointer, countObjectSize(clazz, processPointer));
							} else if (clazz.name.equals("Array")) {
								int arraySize = Utils.byteArrayToInt(getSpace(), processPointer + SvmType.INT.getSize());
								endPointer = copyObject(clazz, remap, pointerQueue, current, currentNext, processPointer, endPointer, arraySize);
							} else {
								endPointer = copyObject(clazz, remap, pointerQueue, current, currentNext, processPointer, endPointer, clazz.fields.length);
							}
							remap.put(processPointer, currentNext);
							currentNext += countObjectSize(clazz, processPointer);
						}
					}
				}
			}
		}
		activeSpace = activeSpace == 0 ? 1 : 0;
		next = currentNext;
	}

	public int copyObject(SvmClass clazz, Map<Integer, Integer> remap, Queue<Integer> queue, byte[] current, int currentNext, int processp, int endp, int fields) {
		// copy header
		copyToNewSpace(getSpace(), current, currentNext, processp, OBJECT_HEADER_BYTES);

		for (int j = 0; j < fields; j++) {
			//SvmField field = clazz.fields[j];
			byte[] val = Utils.getObjectFieldValue(getSpace(), processp, j);
			if (val[SvmType.STORED_TYPE_BYTE_SIZE - 1] == SvmType.POINTER.getIdentByte()) {
				int fieldPointer = Utils.byteArrayToInt(val, 0);

				if (fieldPointer == 0) {
					// null pointer
					Utils.setObjectFieldPointerValue(current, currentNext, j, 0);
					continue;
				}

				Integer mp = remap.get(fieldPointer);
				if (mp != null) {
					Utils.setObjectFieldPointerValue(current, currentNext, j, mp);
				} else {
					queue.add(fieldPointer);

					// discover new object size (endpointer)
					SvmClass fieldClass = vm.permGenSpace.getClass(Utils.byteArrayToInt(getSpace(), fieldPointer));
					int fieldObjectSize = countObjectSize(fieldClass, fieldPointer);

					// copy pointer with value of endPointer;
					Utils.setObjectFieldPointerValue(current, currentNext, j, endp);

					endp += fieldObjectSize;
				}
			} else {
				// copy integer
				Utils.setObjectFieldValue(current, currentNext, j, val);
			}
		}
		return endp;
	}
}
