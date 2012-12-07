package com.codingcrayons.scrappy.vm.syscalls;

import java.io.IOException;
import java.io.Reader;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class CloseFileReader implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		int readerIndex = vm.stack.popInt();
		Reader r = vm.ioHandle.getReader(readerIndex);

		if (r == null) {
			throw new RuntimeException("Reader not exists");
		}

		try {
			r.close();
		} catch (IOException e) {
			throw new RuntimeException("Reader can not be closed");
		}
	}

}
