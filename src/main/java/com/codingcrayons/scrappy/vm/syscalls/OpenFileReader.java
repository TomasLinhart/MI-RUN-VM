package com.codingcrayons.scrappy.vm.syscalls;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.util.Utils;

public class OpenFileReader implements Syscall {

	@Override
	public void call(ScrappyVM vm) throws StackException, ClassNotFoundException, StackOverflowException {
		// String filename pointer
		int pointer = vm.stack.popPointer();
		String fileName = Utils.getStringValue(vm, pointer);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File '" + fileName + "' not found.");
		}

		vm.stack.pushInt(vm.ioHandle.store(br));
	}

}
