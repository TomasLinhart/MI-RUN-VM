package com.codingcrayons.scrappy.vm.syscalls;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;

public interface Syscall {

	public void call(ScrappyVM vm) throws ScrappyVmException;

}
