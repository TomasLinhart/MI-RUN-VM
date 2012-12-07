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
	public Integer[][] ifEqArgs() {
		return new Integer[][] {
				{ 4, 4, 2 },
				{ 2, 4, 4 },
				{ 0, 0, 2 },
				{ -1, 3, 4 }
		};
	}

	@DataProvider
	public Integer[][] ifNeqArgs() {
		return new Integer[][] {
				{ 4, 4, 4 },
				{ 2, 4, 2 },
				{ 0, 0, 4 },
				{ -1, 3, 2 }
		};
	}

	@Test(dataProvider = "ifEqArgs")
	public void testIfEqInstruction(int a, int b, int pc) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		vm.instructionList.addInstruction("ifeq 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), pc);
	}

	@Test(dataProvider = "ifNeqArgs")
	public void testIfNeqInstruction(int a, int b, int pc) throws ScrappyVmException {
		vm.stack.pushInt(a);
		vm.stack.pushInt(b);
		vm.instructionList.addInstruction("ifneq 3"); // 1
		vm.instructionList.addInstruction(null); // 2 if true
		vm.instructionList.addInstruction(null); // 3
		vm.instructionList.addInstruction(null); // 4 if false
		Interpreter.interpret(vm);
		assertEquals(vm.instructionList.getPc(), pc);
	}
}
