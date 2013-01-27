package com.codingcrayons.scrappy.vm.stack;

import org.apache.log4j.Logger;

import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.LookupReturn;
import com.codingcrayons.scrappy.vm.permgen.SvmMethod;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SvmStack {

	public static class StackFrame {

		private final byte[] stackSpace;
		private int next;

		public StackFrame(int size) {
			stackSpace = new byte[size];
			next = 0;
		}

		public StackFrame(StackFrame prevSF, int size, int returnAddress, int objPointer, LookupReturn lr) throws StackOverflowException, StackException {
			stackSpace = new byte[size];
			next = 0;

			// push return address
			pushInt(returnAddress);

			// push locals
			// 0 - object reference
			pushPointer(objPointer);

			// 1 - n (args + other locals)
			next += (lr.method.arguments.length + lr.method.locals.length) * SvmType.TYPE_BYTE_SIZE;

			// current frame is the caller frame
			for (int i = 0; i < lr.args.length; i++) {
				setLocalValue(1 + i, lr.args[i]);
			}

		}

		public void push(byte[] bytes) throws StackOverflowException {
			if (next + bytes.length > stackSpace.length) {
				throw new StackOverflowException();
			}
			for (byte b : bytes) {
				stackSpace[next++] = b;
			}
		}

		public void pushInt(int value) throws StackOverflowException {
			push(Utils.intToByteArray(Utils.createSVMInt(value)));
		}

		public void pushPointer(int pointer) throws StackOverflowException {
			push(Utils.intToByteArray(Utils.createSVMPointer(pointer)));
		}

		public int popInt() throws StackException {
			return Utils.convertSVMTypeToInt(Utils.byteArrayToInt(popValue(), 0));
		}

		public int popPointer() throws StackException {
			return popInt();
		}

		public int getReturnAddress() {
			return Utils.convertSVMTypeToInt(Utils.byteArrayToInt(stackSpace, 0));
		}

		public byte[] getLocalValue(int index) {
			// first is return address, because is here + SvmType.TYPE_BYTE_SIZE
			return Utils.subArray(stackSpace, index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE, SvmType.TYPE_BYTE_SIZE);
		}

		public int getLocalInt(int index) {
			// first is return address, because is here + SvmType.TYPE_BYTE_SIZE
			return Utils.convertSVMTypeToInt(Utils.byteArrayToInt(stackSpace, index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE));
		}

		public int getLocalPointer(int index) {
			return getLocalInt(index);
		}

		private void setLocalValue(int index, byte[] value) {
			int valueStart = index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE;
			for (int i = 0; i < value.length; i++) {
				stackSpace[i + valueStart] = value[i];
			}
		}

		public void setLocalInt(int index, int value) {
			setLocalValue(index, Utils.intToByteArray(Utils.createSVMInt(value)));
		}

		public void setLocalPointer(int index, int pointer) {
			setLocalValue(index, Utils.intToByteArray(Utils.createSVMPointer(pointer)));
		}

		public byte[] popValue() throws StackException {
			next -= SvmType.TYPE_BYTE_SIZE;
			if (next < 0) {
				throw new StackException("Stack Boundary Exceeded");
			}
			return Utils.getValue(stackSpace, next);
		}

		public int getNext() {
			return next;
		}

		public byte[] getStackSpace() {
			return stackSpace;
		}

	}

	private static final Logger logger = Logger.getLogger(SvmStack.class);

	private final StackFrame[] stackFrames;
	private final int stackFrameSize;
	private int csfIndex;

	public SvmStack(int stackFramesCount, int stackFrameSize) {
		logger.info(stackFramesCount + " stack frames by " + stackFrameSize + " bytes ready");

		stackFrames = new StackFrame[stackFramesCount];
		this.stackFrameSize = stackFrameSize;

		// init program stack
		csfIndex = 0;
		stackFrames[csfIndex] = new StackFrame(128);
	}

	public void beginStackFrame(int returnAddress, int objPointer, LookupReturn lr) throws StackOverflowException, StackException {
		if (csfIndex == stackFrames.length - 1) {
			throw new StackOverflowException();
		}
		StackFrame frame = new StackFrame(currentStackFrame(), stackFrameSize, returnAddress, objPointer, lr);
		stackFrames[++csfIndex] = frame;
	}

	/**
	 * 
	 * @return return address
	 */
	public int discardStackFrame() {
		int returnAddress = currentStackFrame().getReturnAddress();
		stackFrames[csfIndex--] = null;
		return returnAddress;
	}

	public void pushInt(int value) throws StackOverflowException {
		currentStackFrame().pushInt(value);
	}

	public void pushPointer(int pointer) throws StackOverflowException {
		currentStackFrame().pushPointer(pointer);
	}

	public int popInt() throws StackException {
		return currentStackFrame().popInt();
	}

	public int popPointer() throws StackException {
		return currentStackFrame().popPointer();
	}

	public int getLocalInt(int index) {
		return currentStackFrame().getLocalInt(index);
	}

	public int getLocalPointer(int index) {
		return currentStackFrame().getLocalPointer(index);
	}

	public byte[] getLocalValue(int index) {
		return currentStackFrame().getLocalValue(index);
	}

	public void setLocalInt(int index, int value) {
		currentStackFrame().setLocalInt(index, value);
	}

	public void setLocalPointer(int index, int pointer) {
		currentStackFrame().setLocalPointer(index, pointer);
	}

	public void setLocalValue(int index, byte[] value) {
		currentStackFrame().setLocalValue(index, value);
	}

	public void push(byte[] bytes) throws StackOverflowException {
		currentStackFrame().push(bytes);
	}

	public byte[] popValue() throws StackException {
		return currentStackFrame().popValue();
	}

	private StackFrame currentStackFrame() {
		if (csfIndex < 0) {
			return null;
		}
		return stackFrames[csfIndex];
	}

	public int getCurrentStackFrameIndex() {
		return csfIndex;
	}

	public StackFrame[] getStackFrames() {
		return stackFrames;
	}

}
