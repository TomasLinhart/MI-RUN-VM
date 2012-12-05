package com.codingcrayons.scrappy.vm.stack;

import com.codingcrayons.scrappy.vm.exceptions.StackException;
import com.codingcrayons.scrappy.vm.exceptions.StackOverflowException;
import com.codingcrayons.scrappy.vm.permgen.SvmField;
import com.codingcrayons.scrappy.vm.permgen.SvmMethod;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

public class SvmStack {

	private static class StackFrame {

		private final byte[] stackSpace;
		private int next;

		public StackFrame(int size) {
			stackSpace = new byte[size];
			next = 0;
		}

		public StackFrame(StackFrame prevSF, int size, int returnAddress, int objPointer, SvmMethod method) throws StackOverflowException, StackException {
			stackSpace = new byte[size];
			next = 0;

			// push return address
			pushInt(returnAddress);

			// push locals
			// 0 - object reference
			pushInt(objPointer);

			// 1 - n (args + other locals)
			for (int i = 0; i < method.arguments.length + method.locals.length; i++) {
				// fill all to 0
				for (int j = 0; j < SvmType.TYPE_BYTE_SIZE; j++) {
					stackSpace[next++] = 0;
				}
			}

			// current frame is the caller frame
			for (int i = method.arguments.length - 1; i >= 0; i--) {
				SvmField argument = method.arguments[i];
				if (argument.type.equals(SvmType.INT)) {
					int value = prevSF.popInt();
					setLocalInt(1 + i, value);
				} else if (argument.type.equals(SvmType.POINTER)) {
					int pointer = prevSF.popPointer();
					setLocalPointer(1 + i, pointer);
				}
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
			push(Utils.intToByteArray(value));
		}

		public void pushPointer(int pointer) throws StackOverflowException {
			pushInt(pointer);
		}

		public int popInt() throws StackException {
			return Utils.byteArrayToInt(popValue(), 0);
		}

		public int popPointer() throws StackException {
			return popInt();
		}

		public int getReturnAddress() {
			return Utils.byteArrayToInt(stackSpace, 0);
		}

		public byte[] getLocalValue(int index) {
			return Utils.subArray(stackSpace, index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE, SvmType.TYPE_BYTE_SIZE);
		}

		public int getLocalInt(int index) {
			return Utils.byteArrayToInt(stackSpace, index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE);
		}

		public int getLocalPointer(int index) {
			return getLocalInt(index);
		}

		public void setLocalInt(int index, int value) {
			byte[] bytes = Utils.intToByteArray(value);
			int valueStart = index * SvmType.TYPE_BYTE_SIZE + SvmType.TYPE_BYTE_SIZE;
			for (int i = 0; i < SvmType.TYPE_BYTE_SIZE; i++) {
				stackSpace[i + valueStart] = bytes[i];
			}
		}

		public void setLocalPointer(int index, int pointer) {
			setLocalInt(index, pointer);
		}

		public byte[] popValue() throws StackException {
			next -= SvmType.TYPE_BYTE_SIZE;
			if (next < 0) {
				throw new StackException("Stack Boundary Exceeded");
			}
			return Utils.getValue(stackSpace, next);
		}

	}

	private final StackFrame[] stackFrames;
	private final int stackFrameSize;
	private int csfIndex;

	public SvmStack(int stackFramesCount, int stackFrameSize) {
		stackFrames = new StackFrame[stackFramesCount];
		this.stackFrameSize = stackFrameSize;

		// init program stack
		csfIndex = 0;
		stackFrames[csfIndex] = new StackFrame(128);
	}

	public void beginStackFrame(int returnAddress, int objPointer, SvmMethod method) throws StackOverflowException, StackException {
		if (csfIndex == stackFrames.length - 1) {
			throw new StackOverflowException();
		}
		StackFrame frame = new StackFrame(currentStackFrame(), stackFrameSize, returnAddress, objPointer, method);
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

}
