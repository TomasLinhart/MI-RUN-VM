package com.codingcrayons.scrappy.vm.syscalls;

public class SyscallSet {

	public static Syscall[] syscalls;

	static {
		syscalls = new Syscall[6];
		syscalls[0] = new PrintInt();
		syscalls[1] = new PrintString();
		syscalls[2] = new AppendString();
		syscalls[3] = new AppendIntToString();
		syscalls[4] = new SplitString();
		syscalls[5] = new StringToInt();
	}

	public static Syscall get(int index) {
		return syscalls[index];
	}

}
