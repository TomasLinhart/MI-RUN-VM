package com.codingcrayons.scrappy.vm.util;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.SvmHeap;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmType;

public class Utils {

	public static int createSVMInt(int value) {
		return createSVMType(value, 0);
	}

	public static int createSVMPointer(int value) {
		return createSVMType(value, 1);
	}

	private static int createSVMType(int value, int t) {
		return (value << 1) | t;
	}

	public static int convertSVMTypeToInt(int value) {
		return value >> 1;
	}

	public static boolean isPointer(byte b) {
		return (b & 0x01) == 1;
	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

	public static int byteArrayToInt(byte[] bytes, int from) {
		return bytes[from] << 24 | (bytes[from + 1] & 0xFF) << 16 | (bytes[from + 2] & 0xFF) << 8 | (bytes[from + 3] & 0xFF);
	}

	public static byte[] subArray(byte[] bytes, int from, int size) {
		byte[] val = new byte[size];
		for (int i = 0; i < size; i++) {
			val[i] = bytes[from + i];
		}
		return val;
	}

	public static byte[] getObjectFieldValue(byte[] bytes, int objectStart, int index) {
		byte[] value = new byte[SvmType.TYPE_BYTE_SIZE];
		int start = index * SvmType.TYPE_BYTE_SIZE + objectStart + SvmHeap.OBJECT_HEADER_BYTES;

		for (int i = 0; i < value.length; i++) {
			value[i] = bytes[start + i];
		}

		return value;
	}

	public static byte[] getValue(byte[] bytes, int from) {
		byte[] value = new byte[SvmType.TYPE_BYTE_SIZE];

		for (int i = 0; i < value.length; i++) {
			value[i] = bytes[from + i];
		}

		return value;
	}

	public static void setObjectFieldValue(byte[] bytes, int objectStart, int index, byte[] value) {
		int start = index * SvmType.TYPE_BYTE_SIZE + objectStart + SvmHeap.OBJECT_HEADER_BYTES;
		for (int i = 0; i < value.length; i++) {
			bytes[start + i] = value[i];
		}
	}

	public static void setObjectFieldIntValue(byte[] bytes, int objectStart, int index, int value) {
		Utils.setObjectFieldValue(bytes, objectStart, index, Utils.intToByteArray(Utils.createSVMInt(value)));
	}

	public static void setObjectFieldPointerValue(byte[] bytes, int objectStart, int index, int value) {
		Utils.setObjectFieldValue(bytes, objectStart, index, Utils.intToByteArray(Utils.createSVMPointer(value)));
	}

	public static void checkNullPointer(int pointer) throws PointerIsNullException {
		if (pointer == 0) {
			throw new PointerIsNullException();
		}
	}

	public static int createStringOnHeap(ScrappyVM vm, SvmClass clazz, byte[] bytes, String value) throws HeapOutOfMemoryException {
		int pointer = vm.heap.allocByteClass(clazz, bytes);

		Utils.setObjectFieldIntValue(vm.heap.getSpace(), pointer, 0, value.length());
		Utils.setObjectFieldIntValue(vm.heap.getSpace(), pointer, 1, bytes.length);

		return pointer;
	}

	public static String getStringValue(ScrappyVM vm, int pointer) {
		return new String(getStringBytes(vm, pointer));
	}

	public static byte[] getStringBytes(ScrappyVM vm, int pointer) {
		int bc = Utils.convertSVMTypeToInt(Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointer, 1), 0));
		int start = pointer + SvmHeap.OBJECT_HEADER_BYTES + 2 * SvmType.TYPE_BYTE_SIZE;
		return Utils.subArray(vm.heap.getSpace(), start, bc);
	}

	public static int getGCState(byte[] bytes, int objectStart) {
		int start = 2 * SvmType.TYPE_BYTE_SIZE + objectStart;
		byte[] value = new byte[SvmType.TYPE_BYTE_SIZE];
		for (int i = 0; i < SvmType.TYPE_BYTE_SIZE; i++) {
			value[i] = bytes[start + i];
		}
		return Utils.byteArrayToInt(value, 0);
	}

	public static int setGCState(byte[] bytes, int objectStart, int value) {
		byte[] v = Utils.intToByteArray(value);
		int start = 2 * SvmType.TYPE_BYTE_SIZE + objectStart;
		for (int i = 0; i < SvmType.TYPE_BYTE_SIZE; i++) {
			bytes[start + i] = v[i];
		}
		return Utils.byteArrayToInt(v, 0);
	}
}
