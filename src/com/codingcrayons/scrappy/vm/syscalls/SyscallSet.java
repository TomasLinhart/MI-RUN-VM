package com.codingcrayons.scrappy.vm.syscalls;

public class SyscallSet {

	public static Syscall[] syscalls;

	static {
		syscalls = new Syscall[2];
		syscalls[0] = new PrintInt();
		syscalls[1] = new PrintString();
	}

	public static Syscall get(int index) {
		return syscalls[index];
	}

}
