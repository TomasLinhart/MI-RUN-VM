package com.codingcrayons.scrappy.vm.syscalls;

public class SyscallSet {

	public static Syscall[] syscalls;

	static {
		syscalls = new Syscall[13];
		syscalls[0] = new PrintInt();
		syscalls[1] = new PrintString();
		syscalls[2] = new AppendString();
		syscalls[3] = new AppendIntToString();
		syscalls[4] = new SplitString();
		syscalls[5] = new StringToInt();
		syscalls[6] = new OpenFileReader();
		syscalls[7] = new OpenFileWriter();
		syscalls[8] = new CloseFileReader();
		syscalls[9] = new CloseFileWriter();
		syscalls[10] = new WriteLine();
		syscalls[11] = new ReadLine();
		syscalls[12] = new ArraySize();
	}

	public static Syscall get(int index) {
		return syscalls[index];
	}

}
