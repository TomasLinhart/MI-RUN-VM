package com.codingcrayons.scrappy.vm.syscalls;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class OpenFileWriter implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		// String filename pointer
		int pointer = vm.stack.popPointer();
		String fileName = Utils.getStringValue(vm, pointer);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			throw new RuntimeException("File '" + fileName + "' can not be opened for write.");
		}

		vm.stack.pushInt(vm.ioHandle.store(bw));
	}

}
