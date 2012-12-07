package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class PrintString implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException {
		int pointer = vm.stack.popPointer();
		System.out.println(Utils.getStringValue(vm, pointer));
	}

}
