package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class StringToInt implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int pointerA = vm.stack.getLocalPointer(0);
		int bcA = Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointerA, 1), 0);
		int start = pointerA + 3 * SvmType.TYPE_BYTE_SIZE;
		byte[] bytes = Utils.subArray(vm.heap.getSpace(), start, bcA);

		String s = new String(bytes);
		int res = 0;
		try {
			res = Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}

		vm.stack.pushInt(res);
	}

}
