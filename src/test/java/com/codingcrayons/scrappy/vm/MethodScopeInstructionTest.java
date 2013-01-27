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
 * Tests for instruction, which operate in scope of one method
 */
public class MethodScopeInstructionTest {

	private ScrappyVM vm;

	@BeforeMethod
	public void setup() throws ScrappyVmException, DocumentException {
		vm = new ScrappyVM(100, 1, 100, new String[] {});
	}

	@DataProvider
	public Object[][] ifArgs() {
		return new Object[][] {
				{ "ifeq", 4, 4, 2 },
				{ "ifeq", 2, 4, 4 },
				{ "ifeq", -1, 3, 4 },
				{ "ifneq", 4, 4, 4 },
				{ "ifneq", 2, 4, 2 },
				{ "ifneq", -1, 3, 2 },
				{ "ifge", 4, 4, 2 },
				{ "ifge", 2, 4, 4 },
				{ "ifge", 0, -1, 2 },
				{ "ifgt", 4, 4, 4 },
				{ "ifgt", 2, 4, 4 },
				{ "ifgt", 3, 1, 2 },
				{ "ifle", 4, 4, 2 },
				{ "ifle", 2, 4, 2 },
				{ "ifle", 3, 1, 4 },
				{ "iflt", 4, 4, 4 },
				{ "iflt", 2, 4, 2 },
				{ "iflt", 3, 1, 4 }
		};
	}

	@Test(dataProvider = "ifArgs")
	public void testIfInstruction(String inst, int a, int b, int pc) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		int is = vm.instructionList.addInstruction(inst + " 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), is + pc);
	}

	@DataProvider
	public Object[][] ifPNullArgs() {
		return new Object[][] {
				{ "ifnull", 0, 2 },
				{ "ifnull", 1, 4 },
				{ "ifnotnull", 0, 4 },
				{ "ifnotnull", 1, 2 }
		};
	}

	@Test(dataProvider = "ifPNullArgs")
	public void testIfPNullInstruction(String inst, int p, int pc) throws ScrappyVmException {
		vm.stack.pushPointer(p);
		int is = vm.instructionList.addInstruction(inst + " 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), is + pc);
	}

	@DataProvider
	public Object[][] iMathArgs() {
		return new Object[][] {
				{ "iadd", 4, 5, 9 },
				{ "iadd", -1, -2, -3 },
				{ "isub", 4, 5, -1 },
				{ "isub", -1, -2, 1 },
				{ "imul", 4, 5, 20 },
				{ "imul", -10, -2, 20 },
				{ "imul", 3, -2, -6 },
				{ "idiv", 10, 5, 2 },
				{ "idiv", -10, -2, 5 },
				{ "idiv", 10, -5, -2 },
				{ "idiv", 10, 3, 3 },
				{ "imod", 10, 5, 0 },
				{ "imod", 10, 3, 1 },
		};
	}

	@Test(dataProvider = "iMathArgs")
	public void testMathInstruction(String inst, int a, int b, int res) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		int is = vm.instructionList.addInstruction(inst);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), res);
	}

	@Test
	public void testDupInstruction() throws ScrappyVmException {
		vm.stack.pushInt(100);
		int is = vm.instructionList.addInstruction("dup");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), 100);
		assertEquals(vm.stack.popInt(), 100);
	}

	@DataProvider
	public Object[][] jumpArgs() {
		return new Object[][] {
				{ 5, 10, 2, 4, 10 },
				{ 5, 10, -2, 0, 5 },
		};
	}

	@Test(dataProvider = "jumpArgs")
	public void testJumpInstruction(int a, int b, int offset, int pcEnd, int expectedStackValue) throws ScrappyVmException {
		vm.instructionList.addInstruction("ipush " + a);
		vm.instructionList.addInstruction(null);
		int is = vm.instructionList.addInstruction("jump " + offset);
		vm.instructionList.addInstruction(null);
		vm.instructionList.addInstruction("ipush " + b);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), expectedStackValue);
		assertEquals(vm.instructionList.getPc(), is + pcEnd);
	}

	@DataProvider
	public Object[][] logOpsArgs() {
		return new Object[][] {
				{ "iand", 5, 10, 1 },
				{ "iand", 0, 10, 0 },
				{ "iand", 5, 0, 0 },
				{ "iand", -1, 10, 0 },
				{ "iand", -1, -10, 0 },
				{ "iand", 1, -10, 0 },
				{ "ior", 5, 10, 1 },
				{ "ior", 0, 10, 1 },
				{ "ior", 5, 0, 1 },
				{ "ior", -1, 10, 1 },
				{ "ior", -1, -10, 0 },
				{ "ior", 1, -10, 1 }
		};
	}

	@Test(dataProvider = "logOpsArgs")
	public void testLogicalOpsInstruction(String inst, int a, int b, int res) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		int is = vm.instructionList.addInstruction(inst);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), res);
	}

	@DataProvider
	public Object[][] logNegArgs() {
		return new Object[][] {
				{ 5, 0 },
				{ 0, 1 },
				{ -5, 1 },
		};
	}

	@Test(dataProvider = "logNegArgs")
	public void testLogicalNegInstruction(int a, int res) throws ScrappyVmException {
		vm.stack.pushInt(a);
		int is = vm.instructionList.addInstruction("ineg");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), res);
	}

	@Test
	public void testPopValue() throws ScrappyVmException {
		vm.stack.pushInt(10);
		vm.stack.pushInt(5);
		int is = vm.instructionList.addInstruction("popvalue");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), 10);
	}

	@DataProvider
	public Object[][] pushArgs() {
		return new Object[][] {
				{ "ppush", SvmType.POINTER.getIdentByte() },
				{ "ipush", SvmType.INT.getIdentByte() }
		};
	}

	@Test(dataProvider = "pushArgs")
	public void testPushInstruction(String inst, byte type) throws ScrappyVmException {
		int push = 10;

		int is = vm.instructionList.addInstruction(inst + " " + push);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		byte[] pop = vm.stack.popValue();
		byte[] num;
		if (type == SvmType.INT.getIdentByte()) {
			num = Utils.intToByteArray(Utils.createSVMInt(10));
		} else {
			num = Utils.intToByteArray(Utils.createSVMPointer(10));
		}

		assertEquals(pop[0], num[0]);
		assertEquals(pop[1], num[1]);
		assertEquals(pop[2], num[2]);
		assertEquals(pop[3], num[3]);
	}

}
