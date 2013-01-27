package com.codingcrayons.scrappy.vm.permgen;

public enum SvmType {

	INT(4, (byte) 0), POINTER(4, (byte) 1), NULL(4, (byte) 2), VOID(0, (byte) 3);

	public static int TYPE_BYTE_SIZE = 4;

	private int size;
	private byte identByte;

	private SvmType(int size, byte identByte) {
		this.size = size;
		this.identByte = identByte;
	}

	public int getSize() {
		return size;
	}

	public byte getIdentByte() {
		return identByte;
	}

}
