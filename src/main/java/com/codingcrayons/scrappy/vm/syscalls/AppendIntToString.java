package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class AppendIntToString implements Syscall {

	private static String STRING_CLASS = "String";

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int pointerA = vm.stack.getLocalPointer(0);
		int intval = vm.stack.getLocalInt(1);

		int bcA = Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointerA, 1), 0);
		int start = pointerA + 3 * SvmType.TYPE_BYTE_SIZE;
		byte[] bytes = Utils.subArray(vm.heap.getSpace(), start, bcA);

		String res = new String(bytes) + intval;

		vm.stack.pushPointer(Utils.createSringOnHeap(vm, vm.permGenSpace.getClass(STRING_CLASS), res.getBytes(), res));
	}

}
