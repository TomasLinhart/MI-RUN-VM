package com.codingcrayons.scrappy.vm;

import static org.testng.Assert.assertEquals;

import org.dom4j.DocumentException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.permgen.SvmClass;
import com.codingcrayons.scrappy.vm.permgen.SvmField;
import com.codingcrayons.scrappy.vm.permgen.SvmMethod;
import com.codingcrayons.scrappy.vm.permgen.SvmType;
import com.codingcrayons.scrappy.vm.util.Utils;

/**
 * Tests for instruction, which operate in method calls
 */
public class MethodCallInstructionTest {

	private ScrappyVM vm;

	@BeforeMethod
	public void setup() throws ScrappyVmException, DocumentException {
		vm = new ScrappyVM(100, 5, 100, new String[] {});

		// stop method processing
		int is = vm.instructionList.addInstruction(null);

		SvmMethod mA = new SvmMethod(
				"test1",
				new SvmField[] { new SvmField("A", SvmType.INT, "Integer"), new SvmField("B", SvmType.POINTER, "Test1") },
				new SvmField[] { new SvmField("C", SvmType.INT, "Integer"), new SvmField("D", SvmType.POINTER, "Test1") },
				SvmType.VOID,
				is);

		is = vm.instructionList.addInstruction("return");

		SvmMethod mB = new SvmMethod(
				"retVoid",
				new SvmField[] {},
				new SvmField[] {},
				SvmType.VOID,
				is);

		is = vm.instructionList.addInstruction("ipush 5");
		vm.instructionList.addInstruction("ireturn");

		SvmMethod mC = new SvmMethod(
				"retInt",
				new SvmField[] {},
				new SvmField[] {},
				SvmType.INT,
				is);

		is = vm.instructionList.addInstruction("ppush 5");
		vm.instructionList.addInstruction("preturn");

		SvmMethod mD = new SvmMethod(
				"retPointer",
				new SvmField[] {},
				new SvmField[] {},
				SvmType.POINTER,
				is);

		is = vm.instructionList.addInstruction("ppush 5");
		vm.instructionList.addInstruction("vreturn");

		SvmMethod mE = new SvmMethod(
				"retAnyPointer",
				new SvmField[] {},
				new SvmField[] {},
				SvmType.POINTER,
				is);

		is = vm.instructionList.addInstruction("ipush 5");
		vm.instructionList.addInstruction("vreturn");

		SvmMethod mF = new SvmMethod(
				"retAnyInteger",
				new SvmField[] {},
				new SvmField[] {},
				SvmType.POINTER,
				is);

		SvmClass clazz = new SvmClass("Test1", new SvmField[] {}, new SvmMethod[] { mA, mB, mC, mD, mE, mF }, null);
		vm.permGenSpace.addClass(clazz);
	}

	@Test
	public void testInvokevirtualInstruction() throws ScrappyVmException {
		assertEquals(vm.stack.getCurrentStackFrameIndex(), 0);

		int a = 7;
		int b = 13;
		int c = 16;
		int d = 19;
		int a2 = 17;
		int b2 = 113;

		int is = vm.instructionList.addInstruction("ipush " + a);
		vm.instructionList.addInstruction("new Test1");
		vm.instructionList.addInstruction("dup");
		vm.instructionList.addInstruction("invokevirtual ::test1::2");
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		byte[] valA = Utils.intToByteArray(Utils.createSVMInt(a));
		byte[] valB = Utils.intToByteArray(Utils.createSVMPointer(1));

		assertEquals(vm.stack.getCurrentStackFrameIndex(), 1);
		// object pointer
		assertEquals(vm.stack.getLocalPointer(0), 1);
		// arg 1 - local 1
		assertEquals(vm.stack.getLocalValue(1), new byte[] { valA[0], valA[1], valA[2], valA[3] });
		// arg 2 - local 2
		assertEquals(vm.stack.getLocalValue(2), new byte[] { valB[0], valB[1], valB[2], valB[3] });

		vm.stack.setLocalInt(1, a2);
		vm.stack.setLocalPointer(2, b2);
		vm.stack.setLocalInt(3, c);
		vm.stack.setLocalPointer(4, d);

		valA = Utils.intToByteArray(Utils.createSVMInt(a2));
		valB = Utils.intToByteArray(Utils.createSVMPointer(b2));
		byte[] valC = Utils.intToByteArray(Utils.createSVMInt(c));
		byte[] valD = Utils.intToByteArray(Utils.createSVMPointer(d));

		assertEquals(vm.stack.getLocalValue(1), new byte[] { valA[0], valA[1], valA[2], valA[3] });
		assertEquals(vm.stack.getLocalValue(2), new byte[] { valB[0], valB[1], valB[2], valB[3] });
		assertEquals(vm.stack.getLocalValue(3), new byte[] { valC[0], valC[1], valC[2], valC[3] });
		assertEquals(vm.stack.getLocalValue(4), new byte[] { valD[0], valD[1], valD[2], valD[3] });

		assertEquals(vm.stack.getLocalInt(1), a2);
		assertEquals(vm.stack.getLocalPointer(2), b2);
		assertEquals(vm.stack.getLocalInt(3), c);
		assertEquals(vm.stack.getLocalPointer(4), d);

		byte[] valE = new byte[] { 1, 2, 3, 4 };
		byte[] valF = new byte[] { 1, 2, 3, 4 };

		vm.stack.setLocalValue(1, valE);
		vm.stack.setLocalValue(2, valF);

		assertEquals(vm.stack.getLocalValue(1), new byte[] { valE[0], valE[1], valE[2], valE[3] });
		assertEquals(vm.stack.getLocalValue(2), new byte[] { valF[0], valF[1], valF[2], valF[3] });

		is = vm.instructionList.addInstruction("ipush " + a2);
		vm.instructionList.addInstruction("istore 1");
		vm.instructionList.addInstruction("ppush " + b2);
		vm.instructionList.addInstruction("pstore 2");
		vm.instructionList.addInstruction("ipush " + c);
		vm.instructionList.addInstruction("istore 3");
		vm.instructionList.addInstruction("ppush " + d);
		vm.instructionList.addInstruction("pstore 4");
		vm.instructionList.addInstruction("iload 1");
		vm.instructionList.addInstruction("pload 2");
		vm.instructionList.addInstruction("iload 3");
		vm.instructionList.addInstruction("pload 4");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.stack.popPointer(), d);
		assertEquals(vm.stack.popPointer(), c);
		assertEquals(vm.stack.popPointer(), b2);
		assertEquals(vm.stack.popPointer(), a2);

		is = vm.instructionList.addInstruction("ipush " + a2);
		vm.instructionList.addInstruction("vstore 1");
		vm.instructionList.addInstruction("ppush " + b2);
		vm.instructionList.addInstruction("vstore 2");
		vm.instructionList.addInstruction("vload 1");
		vm.instructionList.addInstruction("vload 2");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		valE = Utils.intToByteArray(Utils.createSVMInt(a2));
		valF = Utils.intToByteArray(Utils.createSVMPointer(b2));

		assertEquals(vm.stack.popValue(), new byte[] { valF[0], valF[1], valF[2], valF[3] });
		assertEquals(vm.stack.popValue(), new byte[] { valE[0], valE[1], valE[2], valE[3] });
	}

	@Test
	public void testReturnInstruction() throws ScrappyVmException {
		assertEquals(vm.stack.getCurrentStackFrameIndex(), 0);

		int is = vm.instructionList.addInstruction("new Test1");
		vm.instructionList.addInstruction("invokevirtual ::retVoid::0");
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		assertEquals(vm.stack.getCurrentStackFrameIndex(), 0);
		assertEquals(vm.instructionList.getPc(), is + 3);
	}

	@DataProvider
	public Object[][] ipvreturnArgs() {
		return new Object[][] {
				{ "::retInt::", SvmType.INT.getIdentByte() },
				{ "::retPointer::", SvmType.POINTER.getIdentByte() },
				{ "::retAnyPointer::", SvmType.POINTER.getIdentByte() },
				{ "::retAnyInteger::", SvmType.INT.getIdentByte() },
		};
	}

	@Test(dataProvider = "ipvreturnArgs")
	public void testIPVreturnInstruction(String method, byte type) throws ScrappyVmException {
		assertEquals(vm.stack.getCurrentStackFrameIndex(), 0);

		int is = vm.instructionList.addInstruction("new Test1");
		vm.instructionList.addInstruction("invokevirtual " + method);
		vm.instructionList.addInstruction(null);
		vm.instructionList.jump(is);
		Interpreter.interpret(vm);

		byte[] retval = vm.stack.popValue();
		byte[] val;
		if (type == SvmType.INT.getIdentByte()) {
			val = Utils.intToByteArray(Utils.createSVMInt(5));
		} else {
			val = Utils.intToByteArray(Utils.createSVMPointer(5));
		}

		assertEquals(vm.stack.getCurrentStackFrameIndex(), 0);
		assertEquals(vm.instructionList.getPc(), is + 3);
		assertEquals(retval, new byte[] { val[0], val[1], val[2], val[3]});
		assertEquals(Utils.isPointer(retval[3]), type ==  SvmType.POINTER.getIdentByte());
	}

}
