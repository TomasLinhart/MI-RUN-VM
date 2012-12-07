package com.codingcrayons.scrappy.vm.instruction;

import com.codingcrayons.scrappy.vm.ScrappyVM;
import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;

public class LogicalOrIntInstruction extends Instruction {

	@Override
	public void process(ScrappyVM vm, String[] params) throws StackOverflowException, StackException {
		int v1 = vm.stack.popInt();
		int v2 = vm.stack.popInt();

		int res = 1;
		if (v1 <= 0 && v2 <= 0) {
			res = 0;
		}

		vm.stack.pushInt(res);
	}

}
