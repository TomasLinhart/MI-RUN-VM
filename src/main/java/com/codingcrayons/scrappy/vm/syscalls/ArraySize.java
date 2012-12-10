package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class ArraySize implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int pointer = vm.stack.popPointer();
		// next object header value
		int size = Utils.byteArrayToInt(vm.heap.getSpace(), pointer + SvmType.TYPE_BYTE_SIZE);

		System.out.println("Size " + size);

		vm.stack.pushInt(size);
	}

}
