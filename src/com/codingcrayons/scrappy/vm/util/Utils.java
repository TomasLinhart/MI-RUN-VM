package com.codingcrayons.scrappy.vm.util;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.PointerIsNullException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmType;

public class Utils {

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
		int start = index * SvmType.TYPE_BYTE_SIZE + objectStart + SvmType.TYPE_BYTE_SIZE;

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
		int start = index * SvmType.TYPE_BYTE_SIZE + objectStart + SvmType.TYPE_BYTE_SIZE;
		for (int i = 0; i < value.length; i++) {
			bytes[start + i] = value[i];
		}
	}

	public static void checkNullPointer(int pointer) throws PointerIsNullException {
		if (pointer == 0) {
			throw new PointerIsNullException();
		}
	}

	public static int createSringOnHeap(ScrappyVM vm, SvmClass clazz, byte[] bytes, String value) {
		int pointer = vm.heap.allocByteClass(clazz, bytes);

		Utils.setObjectFieldValue(vm.heap.getSpace(), pointer, 0, Utils.intToByteArray(value.length()));
		Utils.setObjectFieldValue(vm.heap.getSpace(), pointer, 1, Utils.intToByteArray(bytes.length));

		return pointer;
	}

}
