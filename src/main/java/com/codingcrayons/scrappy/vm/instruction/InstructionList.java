package com.codingcrayons.scrappy.vm.instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionList {

	// permgen of instuctions - size can be restricted
	// later can be byte array
	private final List<String> instructions;
	/**
	 * program counter
	 */
	private int pc;
	private int ic;

	public InstructionList() {
		instructions = new ArrayList<String>();
		pc = 1;
		ic = 0;
	}

	public int addInstruction(String instruction) {
		instructions.add(instruction);
		return ic++;
	}

	public String nextInstruction() {
		return instructions.get(pc++);
	}

	public void jump(int address) {
		pc = address;
	}

	public int getPc() {
		return pc;
	}

}
