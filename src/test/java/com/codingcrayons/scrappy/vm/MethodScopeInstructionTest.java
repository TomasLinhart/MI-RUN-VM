package com.codingcrayons.scrappy.vm;

import static org.testng.Assert.assertEquals;

import org.dom4j.DocumentException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;

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
	public void testIfEqInstruction(String inst, int a, int b, int pc) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		vm.instructionList.addInstruction(inst + " 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), pc);
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
		vm.instructionList.addInstruction(inst + " 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), pc);
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
	public void testIMathnstruction(String inst, int a, int b, int res) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		vm.instructionList.addInstruction(inst);
		vm.instructionList.addInstruction(null);
		Interpreter.interpret(vm);
		assertEquals(vm.stack.popInt(), res);
	}

}
