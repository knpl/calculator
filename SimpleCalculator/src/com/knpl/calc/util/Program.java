package com.knpl.calc.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import com.knpl.calc.nodes.numbers.Num;

public class Program implements Serializable {
	
	private static final long serialVersionUID = -2503458081984075342L;
	
	private final String name;
	private final int parameterCount;
	private final Num[] constants;
	private final short[] offsets;
	private final byte[] instructions;
	
	private final int stackSize;
	
	public Program(String name, int parameterCount, byte[] instructions,
				   Num[] constants, short[] offsets) {
		this.name = name;
		this.parameterCount = parameterCount;
		this.constants = constants;
		this.offsets = offsets;
		this.instructions = instructions;
		
		stackSize = computeStackSize(0, parameterCount);
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public byte[] getInstructions() {
		return instructions;
	}
	
	public Num[] getConstants() {
		return constants;
	}
	
	public short[] getOffsets() {
		return offsets;
	}
	
	public String getName() {
		return name;
	}
	
	public int getParameterCount() {
		return parameterCount;
	}

	private int computeStackSize(int pc, int size) {
		int max = size;
		while (true) {
			switch(instructions[pc]) {
			case ByteCodes.ADD:
			case ByteCodes.SUB:
			case ByteCodes.MUL:
			case ByteCodes.DIV:
			case ByteCodes.POW:
			case ByteCodes.MAX:
			case ByteCodes.MIN:
			case ByteCodes.POP:
				size--;
				break;
			case ByteCodes.LOADA:
			case ByteCodes.LOADC:
				size++;
				if (size > max) 
					max = size;
				pc++;
				break;
			case ByteCodes.CALL:
				size = computeStackSize(offsets[instructions[pc+1]], size);
				if (size > max) 
					max = size;
				size =  size - instructions[pc+2] + 1;
				pc += 2;
				break;
			case ByteCodes.RET:
				return max;
			}
			pc++;
		}
	}
	
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		
		out.printf("Program name: %s\n", name);
		out.printf("Parameter count: %d\n", parameterCount);
		out.printf("Maximum stack size: %d\n", stackSize);
		
		out.println();
		out.println("Constants:");
		for (int i = 0; i < constants.length; ++i) {
			out.print(i+":\t"+constants[i]+"\n");
		}
		
		out.println();
		out.println("Offsets:");
		for (int i = 0; i < offsets.length; ++i) {
			out.printf(" %d:\t%d\n", i, offsets[i]);
		}
		
		out.println();
		out.println("Instructions:");
		for (int i = 0; i < instructions.length; ++i) {
			byte code = instructions[i];
			out.printf("%4d %s", i, ByteCodes.toString(code));
			switch (code) {
				case ByteCodes.LOADA:
				case ByteCodes.LOADC:
					i+=1;
					out.print(" "+instructions[i]);
					break;
				case ByteCodes.CALL:
					i+=1;
					out.print(" "+instructions[i]);
					i+=1;
					out.print(", "+instructions[i]);
					break;
				case ByteCodes.RET:
					out.println();
				default:
			}
			out.println();
		}
		
		try {
    		return baos.toString("UTF8");
    	}
    	catch (UnsupportedEncodingException e) {
    		return "unsupported encoding";
    	}
	}
}
