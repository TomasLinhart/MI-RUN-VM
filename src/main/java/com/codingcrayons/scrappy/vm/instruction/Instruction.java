package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;


public interface Instruction {

	public void process(ScrappyVM vm) throws ScrappyVmException;

}
