package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class AppendIntToString implements Syscall {

	private static String STRING_CLASS = "String";

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException, HeapOutOfMemoryException {
		int pointer = vm.stack.getLocalPointer(0);
		int intval = vm.stack.getLocalInt(1);

		String res = new String(Utils.getStringBytes(vm, pointer)) + intval;

		vm.stack.pushPointer(Utils.createStringOnHeap(vm, vm.permGenSpace.getClass(STRING_CLASS), res.getBytes(), res));
	}

}
