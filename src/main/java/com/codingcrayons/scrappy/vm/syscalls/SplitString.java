package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.SvmHeap;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SplitString implements Syscall {

	private static String STRING_CLASS = "String";

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException, HeapOutOfMemoryException {
		int pointerA = vm.stack.getLocalPointer(0);
		int bcA = Utils.convertSVMTypeToInt(Utils.byteArrayToInt(Utils.getObjectFieldValue(vm.heap.getSpace(), pointerA, 1), 0));
		int start = pointerA + SvmHeap.OBJECT_HEADER_BYTES + 2 * SvmType.TYPE_BYTE_SIZE;
		byte[] bytes = Utils.subArray(vm.heap.getSpace(), start, bcA);
		String toSplit = new String(bytes);
		String[] splitted = toSplit.split(" ");

		int arrPointer = vm.heap.allocArray(splitted.length);

		for (int i = 0; i < splitted.length; i++) {
			int partPointer = Utils.createStringOnHeap(vm, vm.permGenSpace.getClass(STRING_CLASS), splitted[i].getBytes(),
					splitted[i]);
			Utils.setObjectFieldPointerValue(vm.heap.getSpace(), arrPointer, i, partPointer);
		}

		vm.stack.pushPointer(arrPointer);
	}

}
