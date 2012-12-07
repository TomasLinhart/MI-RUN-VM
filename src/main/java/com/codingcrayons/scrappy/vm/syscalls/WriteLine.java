package com.codingcrayons.scrappy.vm.syscalls;

import java.io.BufferedWriter;
import java.io.IOException;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class WriteLine implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		// String line
		int writerIndex = vm.stack.popInt();
		int pointer = vm.stack.popPointer();
		BufferedWriter w = vm.ioHandle.getWriter(writerIndex);

		if (w == null) {
			throw new RuntimeException("Writer not exists");
		}

		String line = Utils.getStringValue(vm, pointer);
		try {
			w.write(line + "\n");
			w.flush();
		} catch (IOException e) {
			throw new RuntimeException("Line " + line + "can not be writed");
		}
	}

}
