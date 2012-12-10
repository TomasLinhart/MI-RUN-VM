package com.codingcrayons.scrappy.vm;

import static org.testng.Assert.assertEquals;

import org.dom4j.DocumentException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

/**
 * Tests for instructions, which operates with heap
 */
public class HeapRelatedInstructionTest {

	private ScrappyVM vm;

	@BeforeMethod
	public void setup() throws ScrappyVmException, DocumentException {
		vm = new ScrappyVM(200, 1, 100, new String[] { "test-classes/Test.xml" });
	}

	@Test
	public void testNewInstruction() throws ScrappyVmException {
		int start = 1;
		assertEquals(vm.heap.getNext(), start);

		int is = vm.instructionList.addInstruction("new Test");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.stack.popPointer(), start); // object pointer on heap

		// class ref to permgen
		assertEquals(Utils.byteArrayToInt(vm.heap.getSpace(), 1), 4); // 4 libs

		// 8 header + 5 * num of fields
		assertEquals(vm.heap.getNext(), start + SvmHeap.OBJECT_HEADER_BYTES + 5 * 2);
	}

	@DataProvider
	public Object[][] setFieldArgs() {
		return new Object[][] {
				{ "ipush", 0, SvmType.INT.getIdentByte(), true },
				{ "ppush", 0, SvmType.POINTER.getIdentByte(), true },
				{ "ipush", 0, SvmType.INT.getIdentByte(), false },
				{ "ppush", 0, SvmType.POINTER.getIdentByte(), false },
				{ "ipush", 1, SvmType.INT.getIdentByte(), true },
				{ "ppush", 1, SvmType.POINTER.getIdentByte(), true },
				{ "ipush", 1, SvmType.INT.getIdentByte(), false },
				{ "ppush", 1, SvmType.POINTER.getIdentByte(), false },
		};
	}

	@Test(dataProvider = "setFieldArgs")
	public void testSetFieldInstruction(String inst, int field, byte type, boolean inline) throws ScrappyVmException {
		int start = 1;
		assertEquals(vm.heap.getNext(), start);

		int is = 0;
		if (!inline) {
			is = vm.instructionList.addInstruction("ipush " + field);
		}
		int x = vm.instructionList.addInstruction(inst + " 5");
		if (inline) {
			is = x;
		}
		vm.instructionList.addInstruction("new Test");
		vm.instructionList.addInstruction("setfield" + (inline ? " " + field : ""));
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(Utils.byteArrayToInt(vm.heap.getSpace(), start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE), 5);
		assertEquals(vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + (1 + field) * SvmType.STORED_TYPE_BYTE_SIZE - 1], type);
	}

	@DataProvider
	public Object[][] getFieldArgs() {
		return new Object[][] {
				{ 0, SvmType.INT.getIdentByte(), true },
				{ 0, SvmType.INT.getIdentByte(), false },
				{ 0, SvmType.POINTER.getIdentByte(), true },
				{ 0, SvmType.POINTER.getIdentByte(), false },
				{ 1, SvmType.INT.getIdentByte(), true },
				{ 1, SvmType.INT.getIdentByte(), false },
				{ 1, SvmType.POINTER.getIdentByte(), true },
				{ 1, SvmType.POINTER.getIdentByte(), false },
		};
	}

	@Test(dataProvider = "getFieldArgs")
	public void testGetFieldInstruction(int field, byte type, boolean inline) throws ScrappyVmException {
		int start = 1;
		assertEquals(vm.heap.getNext(), start);

		int is = 0;
		if (!inline) {
			is = vm.instructionList.addInstruction("ipush " + field);
		}
		int x = vm.instructionList.addInstruction("new Test");
		if (inline) {
			is = x;
		}
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		// manually set heap value
		byte[] value = Utils.intToByteArray(5);
		vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE] = value[0];
		vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE + 1] = value[1];
		vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE + 2] = value[2];
		vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE + 3] = value[3];
		vm.heap.getSpace()[start + SvmHeap.OBJECT_HEADER_BYTES + field * SvmType.STORED_TYPE_BYTE_SIZE + 4] = type;

		is = vm.instructionList.addInstruction("getfield" + (inline ? " " + field : ""));
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.stack.popValue(), new byte[] { value[0], value[1], value[2], value[3], type });
	}

	@DataProvider
	public Object[][] newarrayArgs() {
		return new Object[][] {
				{ 12, true },
				{ 12, false },
				{ 17, true },
				{ 17, false },
		};
	}

	@Test(dataProvider = "newarrayArgs")
	public void testNewarrayInstruction(int size, boolean inline) throws ScrappyVmException {
		int start = 1;
		assertEquals(vm.heap.getNext(), start);

		int is = 0;
		if (!inline) {
			is = vm.instructionList.addInstruction("ipush " + size);
		}
		int x = vm.instructionList.addInstruction("newarray" + (inline ? " " + size : ""));
		if (inline) {
			is = x;
		}
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.stack.popPointer(), start); // object pointer on heap
		assertEquals(vm.heap.getNext(), start + SvmHeap.OBJECT_HEADER_BYTES + size * SvmType.STORED_TYPE_BYTE_SIZE);
		assertEquals(Utils.byteArrayToInt(vm.heap.getSpace(), start + 4), size);
	}

	@Test
	public void testNewstringInstruction() throws ScrappyVmException {
		int start = 1;
		assertEquals(vm.heap.getNext(), start);

		String param = "lorem impsum dolor sit amet";
		int is = vm.instructionList.addInstruction("newstring #" + param);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.heap.getNext(), start + SvmHeap.OBJECT_HEADER_BYTES + 2 * SvmType.STORED_TYPE_BYTE_SIZE + param.getBytes().length);
		assertEquals(Utils.byteArrayToInt(vm.heap.getSpace(), start + SvmHeap.OBJECT_HEADER_BYTES), param.length());
		assertEquals(Utils.byteArrayToInt(vm.heap.getSpace(), start + SvmHeap.OBJECT_HEADER_BYTES + SvmType.STORED_TYPE_BYTE_SIZE), param.getBytes().length);
		assertEquals(Utils.getStringBytes(vm, start), param.getBytes());
		assertEquals(Utils.getStringValue(vm, start), param);
	}

}
