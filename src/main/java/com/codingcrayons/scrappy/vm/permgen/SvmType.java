package com.codingcrayons.scrappy.vm.permgen;

public enum SvmType {

	INT(4), POINTER(4), NULL(4), VOID(0);

	public static int TYPE_BYTE_SIZE = 4;

	private int size;

	private SvmType(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}
