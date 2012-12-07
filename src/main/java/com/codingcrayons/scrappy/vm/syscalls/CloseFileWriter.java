package com.codingcrayons.scrappy.vm.syscalls;

import java.io.IOException;
import java.io.Writer;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class CloseFileWriter implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int writerIndex = vm.stack.popInt();
		Writer w = vm.ioHandle.getWriter(writerIndex);

		if (w == null) {
			throw new RuntimeException("Writer not exists");
		}

		try {
			w.close();
		} catch (IOException e) {
			throw new RuntimeException("Writer can not be closed");
		}
	}

}
