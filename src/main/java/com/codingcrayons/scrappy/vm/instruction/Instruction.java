package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;

public abstract class Instruction {

	public void process(ScrappyVM vm, String instruction) throws ScrappyVmException {
		int i = instruction.indexOf(" ");
		if (i > 0) {
			String sub = instruction.substring(i + 1);
			if (sub.startsWith("#")) {
				process(vm, new String[] { sub });
			} else {
				process(vm, sub.split(" "));
			}
		} else {
			process(vm, (String[]) null);
		}
	}

	public abstract void process(ScrappyVM vm, String[] params) throws ScrappyVmException;

}
