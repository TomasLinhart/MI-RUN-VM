package com.codingcrayons.scrappy.vm.syscalls;

import java.io.BufferedReader;
import java.io.IOException;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class ReadLine implements Syscall {

	private static String STRING_CLASS = "String";

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int readerIndex = vm.stack.popInt();
		BufferedReader r = vm.ioHandle.getReader(readerIndex);

		if (r == null) {
			throw new RuntimeException("Reader not exists");
		}

		String line;
		try {
			line = r.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Line can not be readed");
		}
		vm.stack.pushPointer(Utils.createSringOnHeap(vm, vm.permGenSpace.getClass(STRING_CLASS), line.getBytes(), line));
	}

}
