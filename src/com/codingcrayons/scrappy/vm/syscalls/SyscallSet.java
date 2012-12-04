package com.codingcrayons.scrappy.vm.syscalls;


public class SyscallSet {

	public static Syscall[] syscalls;

	static {
		syscalls = new Syscall[1];
		syscalls[0] = new PrintInt();
	}

	public static Syscall get(int index) {
		return syscalls[index];
	}

}
