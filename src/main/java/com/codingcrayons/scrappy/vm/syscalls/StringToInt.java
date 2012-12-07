package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class StringToInt implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int pointer = vm.stack.getLocalPointer(0);
		String s = Utils.getStringValue(vm, pointer);

		int res = 0;
		try {
			res = Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}

		vm.stack.pushInt(res);
	}

}
