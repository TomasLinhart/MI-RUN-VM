package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class PrintString implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException {
		int pointer = vm.stack.popPointer();
		int bc = Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointer, 1), 0);

		byte[] bytes = new byte[bc];

		// class ref + string length + bytes count
		int start = pointer + 3 * SvmType.TYPE_BYTE_SIZE;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = vm.heap.getSpace()[start + i];
		}

		System.out.println(new String(bytes));
	}

}
