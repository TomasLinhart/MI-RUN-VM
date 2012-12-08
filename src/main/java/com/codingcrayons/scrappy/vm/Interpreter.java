package com.codingcrayons.scrappy.vm;

import java.util.HashMap;

import com.codingcrayons.scrappy.vm.exceptions.ScrappyVmException;
import com.codingcrayons.scrappy.vm.exceptions.UnknownInstructionException;
import com.codingcrayons.scrappy.vm.instruction.AddIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.DivIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.DupInstruction;
import com.codingcrayons.scrappy.vm.instruction.GetFieldInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntEqInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntGeInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntGtInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntLeInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntLtInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfIntNeqInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfPointerNotNullInstruction;
import com.codingcrayons.scrappy.vm.instruction.IfPointerNullInstruction;
import com.codingcrayons.scrappy.vm.instruction.Instruction;
import com.codingcrayons.scrappy.vm.instruction.InvokevirtualInstruction;
import com.codingcrayons.scrappy.vm.instruction.JumpInstruction;
import com.codingcrayons.scrappy.vm.instruction.LoadIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.LoadPointerInstruction;
import com.codingcrayons.scrappy.vm.instruction.LogicalAndIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.LogicalNegIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.LogicalOrIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.ModIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.MulIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.NewArrayInstruction;
import com.codingcrayons.scrappy.vm.instruction.NewInstruction;
import com.codingcrayons.scrappy.vm.instruction.NewStringInstruction;
import com.codingcrayons.scrappy.vm.instruction.PopValueInstruction;
import com.codingcrayons.scrappy.vm.instruction.PushIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.PushPointerInstruction;
import com.codingcrayons.scrappy.vm.instruction.ReturnInstruction;
import com.codingcrayons.scrappy.vm.instruction.ReturnIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.ReturnPointerInstruction;
import com.codingcrayons.scrappy.vm.instruction.SetFieldInstruction;
import com.codingcrayons.scrappy.vm.instruction.StoreIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.StorePointerInstruction;
import com.codingcrayons.scrappy.vm.instruction.SubIntInstruction;
import com.codingcrayons.scrappy.vm.instruction.SyscallInstruction;

public class Interpreter {

	private static final HashMap<String, Instruction> instructionMap;

	static {
		instructionMap = new HashMap<String, Instruction>(35);
		instructionMap.put("ipush", new PushIntInstruction());
		instructionMap.put("ppush", new PushPointerInstruction());
		instructionMap.put("syscall", new SyscallInstruction());
		instructionMap.put("new", new NewInstruction());
		instructionMap.put("invokevirtual", new InvokevirtualInstruction());
		instructionMap.put("getfield", new GetFieldInstruction());
		instructionMap.put("setfield", new SetFieldInstruction());
		instructionMap.put("ireturn", new ReturnIntInstruction());
		instructionMap.put("preturn", new ReturnPointerInstruction());
		instructionMap.put("return", new ReturnInstruction());
		instructionMap.put("iadd", new AddIntInstruction());
		instructionMap.put("isub", new SubIntInstruction());
		instructionMap.put("imul", new MulIntInstruction());
		instructionMap.put("idiv", new DivIntInstruction());
		instructionMap.put("imod", new ModIntInstruction());
		instructionMap.put("ifeq", new IfIntEqInstruction());
		instructionMap.put("ifge", new IfIntGeInstruction());
		instructionMap.put("ifgt", new IfIntGtInstruction());
		instructionMap.put("ifle", new IfIntLeInstruction());
		instructionMap.put("iflt", new IfIntLtInstruction());
		instructionMap.put("ifneq", new IfIntNeqInstruction());
		instructionMap.put("ifnotnull", new IfPointerNotNullInstruction());
		instructionMap.put("ifnull", new IfPointerNullInstruction());
		instructionMap.put("newarray", new NewArrayInstruction());
		instructionMap.put("iload", new LoadIntInstruction());
		instructionMap.put("pload", new LoadPointerInstruction());
		instructionMap.put("istore", new StoreIntInstruction());
		instructionMap.put("pstore", new StorePointerInstruction());
		instructionMap.put("jump", new JumpInstruction());
		instructionMap.put("newstring", new NewStringInstruction());
		instructionMap.put("dup", new DupInstruction());
		instructionMap.put("iand", new LogicalAndIntInstruction());
		instructionMap.put("ior", new LogicalOrIntInstruction());
		instructionMap.put("ineg", new LogicalNegIntInstruction());
		instructionMap.put("popvalue", new PopValueInstruction());
	}

	public static void interpret(ScrappyVM vm) throws ScrappyVmException {
		String instruction = null;
		while ((instruction = vm.instructionList.nextInstruction()) != null) {
			processInstruction(vm, instruction);
		}
	}

	public static void processInstruction(ScrappyVM vm, String instructionLine) throws ScrappyVmException {
		String[] args = null;
		String instructionName = instructionLine;

		int i = instructionLine.indexOf(" ");
		if (i > 0) {
			instructionName = instructionLine.substring(0, i);
			String sub = instructionLine.substring(i + 1);
			if (sub.startsWith("#")) {
				args = new String[] { sub.substring(1) };
			} else {
				args = sub.split(" ");
			}
		} else {
			args = new String[] {};
		}
		Instruction instruction = instructionMap.get(instructionName);
		if (instruction == null) {
			throw new UnknownInstructionException(instructionLine);
		}
		instruction.process(vm, args);
	}

}
