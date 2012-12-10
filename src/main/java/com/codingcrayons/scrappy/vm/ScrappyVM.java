package com.codingcrayons.scrappy.vm;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.codingcrayons.scrappy.vm.exceptions.ClassNotFoundException;
import com.codingcrayons.scrappy.vm.exceptions.HeapOutOfMemoryException;
import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.instruction.InstructionList;
import com.codingcrayons.scrappy.vm.io.IOHandle;
import com.codingcrayons.scrappy.vm.permgen.PermGenSpace;
import com.codingcrayons.scrappy.vm.stack.SvmStack;
import com.codingcrayons.scrappy.vm.util.Utils;

public class ScrappyVM {

	private static final Logger logger = Logger.getLogger(ScrappyVM.class);

	public InstructionList instructionList;
	public PermGenSpace permGenSpace;
	public SvmHeap heap;
	public SvmStack stack;
	public IOHandle ioHandle;

	public ScrappyVM(int heapSize, int stackFramesCount, int stackFrameSize, String[] classFiles) throws ScrappyVmException, DocumentException {
		logger.info("ScrappyVM starting");

		int x = Utils.byteArrayToInt(new byte[] { 0, 0, 2, -26 }, 0);
		System.out.println(x);

		x = Utils.byteArrayToInt(new byte[] { 0, 0, 1, -37 }, 0);
		System.out.println(x);

		instructionList = new InstructionList();
		permGenSpace = new PermGenSpace(classFiles, instructionList);
		stack = new SvmStack(stackFramesCount, stackFrameSize);
		heap = new SvmHeap(heapSize, this);
		ioHandle = new IOHandle();
	}

	public void start() throws ScrappyVmException {
		init();
		Interpreter.interpret(this);
	}

	public void init() throws ClassNotFoundException, HeapOutOfMemoryException {
		int address = heap.alloc(permGenSpace.getClass("EntryPoint"));

		int start = instructionList.addInstruction("ppush " + 0);
		instructionList.addInstruction("ipush " + address);
		instructionList.addInstruction("invokevirtual Entry:Array");
		instructionList.addInstruction(null);
		instructionList.jump(start);
	}

	/**
	 * 
	 * @param args
	 * @throws ScrappyVmException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws ScrappyVmException, DocumentException {
		if (args.length < 4) {
			System.err.println("Args: heap size, max stack frames, stack frame size, scrappy class file, [next class files]");
			return;
		}

		int heapSize = parseInt(args[0], 1024);
		int stackFramesCount = parseInt(args[1], 64);
		int stackFrameSize = parseInt(args[2], 512);

		String[] files = new String[args.length - 3];
		for (int i = 0; i < args.length - 3; i++) {
			files[i] = args[i + 3];
		}

		(new ScrappyVM(heapSize, stackFramesCount, stackFrameSize, files)).start();

	}

	private static int parseInt(String s, int value) {
		try {
			value = Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}
		return value;
	}

}
