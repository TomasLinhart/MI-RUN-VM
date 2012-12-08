package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.SvmHeap;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class AppendString implements Syscall {

	private static String STRING_CLASS = "String";

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int pointerA = vm.stack.getLocalPointer(0);
		int pointerB = vm.stack.getLocalPointer(1);

		int bcA = Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointerA, 1), 0);
		int bcB = Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointerB, 1), 0);

		byte[] bytes = new byte[bcA + bcB];

		int i;
		// class ref + string length + bytes count
		int start = pointerA + SvmHeap.OBJECT_HEADER_BYTES + 2 * SvmType.STORED_TYPE_BYTE_SIZE;
		for (i = 0; i < bcA; i++) {
			bytes[i] = vm.heap.getSpace()[start + i];
		}

		// class ref + string length + bytes count
		start = pointerB + SvmHeap.OBJECT_HEADER_BYTES + 2 * SvmType.STORED_TYPE_BYTE_SIZE;
		for (i = 0; i < bcB; i++) {
			bytes[bcA + i] = vm.heap.getSpace()[start + i];
		}

		vm.stack.pushPointer(Utils.createSringOnHeap(vm, vm.permGenSpace.getClass(STRING_CLASS), bytes, new String(bytes)));
	}

}
