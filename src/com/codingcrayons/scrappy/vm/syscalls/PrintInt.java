package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;

public class PrintInt implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException {
		int value = vm.stack.popInt();
		System.out.println(value);
	}

}
