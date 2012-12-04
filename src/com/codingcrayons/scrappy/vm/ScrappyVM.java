package com.codingcrayons.scrappy.vm;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.instruction.InstructionList;
import com.codingcrayons.scrappy.vm.permgen.PermGenSpace;
import com.codingcrayons.scrappy.vm.stack.SvmStack;

public class ScrappyVM {

	public InstructionList instructionList;
	public PermGenSpace permGenSpace;
	public SvmHeap heap;
	public SvmStack stack;

	public ScrappyVM(String[] classFiles) throws ScrappyVmException {
		instructionList = new InstructionList();
		permGenSpace = new PermGenSpace(classFiles, instructionList);
		stack = new SvmStack(100, 1024);
		heap = new SvmHeap(1024, this);

		Interpreter.interpret(this);
	}

	public void init() throws ClassNotFoundException {
		// int address = heap.alloc(permGenSpace.getClass("Main"));

	}

	/**
	 * 
	 * @param args scrappy bytecode files
	 * @throws ScrappyVmException 
	 */
	public static void main(String[] args) throws ScrappyVmException {
		new ScrappyVM(args);
	}

}
