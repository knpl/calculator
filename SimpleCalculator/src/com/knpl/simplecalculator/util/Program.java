package com.knpl.simplecalculator.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Program implements Serializable {
	
	private static final long serialVersionUID = -2503458081984075342L;
	
	private final String name;
	private final double[] constants;
	private final short[] offsets;
	private final byte[] instructions;
	private final int parameterCount;
	private final int stackSize;
	
	public Program(String name, byte[] instructions, List<Double> constants, List<Integer> offsets,
				   int parameterCount, int stackSize) {
		this.name = name;
		
		this.constants = new double[constants.size()];
		for (int i = 0; i < this.constants.length; ++i) {
			this.constants[i] = constants.get(i); 
		}
		
		this.offsets = new short[offsets.size()];
		for (int i = 0; i < this.offsets.length; ++i) {
			this.offsets[i] = (short)(int)offsets.get(i); 
		}
		
		this.instructions = instructions;
		this.parameterCount = parameterCount;
		this.stackSize = stackSize;
	}
	
	public String getName() {
		return name;
	}
	
	public int getParameterCount() {
		return parameterCount;
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public double evaluate(List<Double> args) {
		double[] stack = new double[stackSize + parameterCount];
		
		int size = args.size();
		for (int i = 0; i < size; ++i) {
			stack[i] = args.get(i);
		}
		
		eval(stack, 0, size);
		
		return stack[0];
	}
	
	public void evaluate(float[] dst, int dindex, int dstep,
						 float[] src, int sindex, int sstep)
	{	
		int n = (src.length / sstep);
		
		float[] stacks = new float[n * (stackSize + parameterCount)];
		for (int i = 0; i < parameterCount; ++i) {
			int row = i*n;
			for (int j = 0; j < n; ++j) {
				stacks[row + j] = src[j*sstep + sindex];
			}
		}
		
		simd(stacks, n, 0, parameterCount);
		
		for (int j = 0; j < n; ++j) {
			dst[j*dstep + dindex] = stacks[j];
		}
	}
	
	private int eval(double[] stack, int pc, int sp) {
		int oldsp = sp;
		
		while (true) {
			switch (instructions[pc]) {
			case ByteCodes.MINUS:
				stack[sp-1] = -stack[sp-1];
				break;
				
			case ByteCodes.ADD:
				stack[sp-2] += stack[sp-1];
				sp -= 1;
				break;
			
			case ByteCodes.SUB:
				stack[sp-2] -= stack[sp-1];
				sp -= 1;
				break;
				
			case ByteCodes.MUL:
				stack[sp-2] *= stack[sp-1];
				sp -= 1;
				break;
				
			case ByteCodes.DIV:
				stack[sp-2] /= stack[sp-1];
				sp -= 1;
				break;
				
			case ByteCodes.POW:
				stack[sp-2] = Math.pow(stack[sp-2], stack[sp-1]);
				sp -= 1;
				break;
				
			case ByteCodes.ABS:
				stack[sp-1] = Math.abs(stack[sp-1]);
				break;
				
			case ByteCodes.MIN:
				stack[sp-2] = Math.min(stack[sp-2], stack[sp-1]);
				sp -= 1;
				break;
				
			case ByteCodes.MAX:
				stack[sp-2] = Math.max(stack[sp-2], stack[sp-1]);
				sp -= 1;
				break;
				
			case ByteCodes.SQRT:
				stack[sp-1] = Math.sqrt(stack[sp-1]);
				break;
				
			case ByteCodes.EXP:
				stack[sp-1] = Math.exp(stack[sp-1]);
				break;
				
			case ByteCodes.LOG:
				stack[sp-1] = Math.log(stack[sp-1]);
				break;
				
			case ByteCodes.SINH:
				stack[sp-1] = Math.sinh(stack[sp-1]);
				break;
				
			case ByteCodes.COSH:
				stack[sp-1] = Math.cosh(stack[sp-1]);
				break;
				
			case ByteCodes.SIN:
				stack[sp-1] = Math.sin(stack[sp-1]);
				break;
				
			case ByteCodes.COS:
				stack[sp-1] = Math.cos(stack[sp-1]);
				break;
				
			case ByteCodes.TAN:
				stack[sp-1] = Math.tan(stack[sp-1]);
				break;
				
			case ByteCodes.ASIN:
				stack[sp-1] = Math.asin(stack[sp-1]);
				break;
				
			case ByteCodes.ACOS:
				stack[sp-1] = Math.acos(stack[sp-1]);
				break;
				
			case ByteCodes.ATAN:
				stack[sp-1] = Math.atan(stack[sp-1]);
				break;
				
			case ByteCodes.LOADC:
				pc += 1;
				stack[sp] = constants[instructions[pc]];
				sp += 1;
				break;
			
			case ByteCodes.POP:
				sp -= 1;
				break;
				
			case ByteCodes.LOADA:
				pc += 1;
				stack[sp] = stack[oldsp - instructions[pc]];
				sp += 1;
				break;
				
			case ByteCodes.CALL:
				pc += 1;
				sp = sp - eval(stack, offsets[instructions[pc]], sp) + 1;
				break;
			
			case ByteCodes.RET:
				pc += 1;
				int nargs = instructions[pc];
				stack[sp - 1 - nargs] = stack[sp - 1];
				return nargs;
				
			default:
				return -1;
			}
			
			pc += 1;
		}
	}
	
	private void simdMinus(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = -stacks[i];
		}
	}
	
	private void simdAdd(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] += stacks[i + n];
		}
	}
	
	private void simdSub(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] -= stacks[i + n];
		}
	}
	
	private void simdMul(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] *= stacks[i + n];
		}
	}
	
	private void simdDiv(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] /= stacks[i + n];
		}
	}
	
	private void simdPow(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.pow(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdAbs(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = Math.abs(stacks[i]);
		}
	}
	
	private void simdMin(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = Math.min(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdMax(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 2);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = Math.max(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdSqrt(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.sqrt(stacks[i]);
		}
	}
	
	private void simdExp(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.exp(stacks[i]);
		}
	}
	
	private void simdLog(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.log(stacks[i]);
		}
	}
	
	private void simdSinh(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.sinh(stacks[i]);
		}
	}
	
	private void simdCosh(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.cosh(stacks[i]);
		}
	}
	
	private void simdSin(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.sin(stacks[i]);
		}
	}
	
	private void simdCos(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.cos(stacks[i]);
		}
	}
	
	private void simdTan(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.tan(stacks[i]);
		}
	}
	
	private void simdAsin(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.asin(stacks[i]);
		}
	}
	
	private void simdAcos(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.acos(stacks[i]);
		}
	}
	
	private void simdAtan(float[] stacks, int n, int sp) {
		int dstIndex = n * (sp - 1);
		for (int i = dstIndex; i < dstIndex + n; i += 1) {
			stacks[i] = (float)Math.atan(stacks[i]);
		}
	}
	
	private void simdLoadc(float[] stacks, int n, int sp, float constant) {
		int index = n * sp;
		for (int i = index; i < index + n; i += 1) {
			stacks[i] = constant;
		}
	}
	
	private void simdLoada(float[] stacks, int n, int sp, int oldsp, int paramIndex) {
		int srcIndex = n * (oldsp - paramIndex);
		int dstIndex = n * sp;
		
		for (int i = 0; i < n; i += 1) {
			stacks[dstIndex] = stacks[srcIndex];
			dstIndex += 1;
			srcIndex += 1;
		}
	}
	
	private void simdRet(float[] stacks, int n, int sp, int nargs) {
		int srcIndex = n * (sp - 1);
		int dstIndex = n * (sp - 1 - nargs);
		
		for (int i = 0; i < n; i += 1) {
			stacks[dstIndex] = stacks[srcIndex];
			dstIndex += 1;
			srcIndex += 1;
		}
	}
	
	private int simd(float[] stacks, int n, int pc, int sp) {
		int oldsp = sp;
		
		while (true) {
			switch (instructions[pc]) {
			case ByteCodes.MINUS:
				simdMinus(stacks, n, sp);
				break;
				
			case ByteCodes.ADD:
				simdAdd(stacks, n, sp);
				sp -= 1;
				break;
			
			case ByteCodes.SUB:
				simdSub(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.MUL:
				simdMul(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.DIV:
				simdDiv(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.POW:
				simdPow(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.ABS:
				simdAbs(stacks, n, sp);
				break;
				
			case ByteCodes.MIN:
				simdMin(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.MAX:
				simdMax(stacks, n, sp);
				sp -= 1;
				break;
				
			case ByteCodes.SQRT:
				simdSqrt(stacks, n, sp);
				break;
				
			case ByteCodes.EXP:
				simdExp(stacks, n, sp);
				break;
				
			case ByteCodes.LOG:
				simdLog(stacks, n, sp);
				break;
				
			case ByteCodes.SINH:
				simdSinh(stacks, n, sp);
				break;
				
			case ByteCodes.COSH:
				simdCosh(stacks, n, sp);
				break;
				
			case ByteCodes.SIN:
				simdSin(stacks, n, sp);
				break;
				
			case ByteCodes.COS:
				simdCos(stacks, n, sp);
				break;
				
			case ByteCodes.TAN:
				simdTan(stacks, n, sp);
				break;
				
			case ByteCodes.ASIN:
				simdAsin(stacks, n, sp);
				break;
				
			case ByteCodes.ACOS:
				simdAcos(stacks, n, sp);
				break;
				
			case ByteCodes.ATAN:
				simdAtan(stacks, n, sp);
				break;
				
			case ByteCodes.LOADC:
				pc += 1;
				simdLoadc(stacks, n, sp, (float)constants[instructions[pc]]);
				sp += 1;
				break;
			
			case ByteCodes.POP:
				sp -= 1;
				break;
				
			case ByteCodes.LOADA:
				pc += 1;
				simdLoada(stacks, n, sp, oldsp, instructions[pc]);
				sp += 1;
				break;
				
			case ByteCodes.CALL:
				pc += 1;
				sp = sp - simd(stacks, n, offsets[instructions[pc]], sp) + 1;
				break;
			
			case ByteCodes.RET:
				pc += 1;
				int nargs = instructions[pc];
				simdRet(stacks, n, sp, nargs);
				return nargs;
				
			default:
				return -1;
			}
			pc += 1;
		}
	}
	
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		
		out.printf("Program name: %s\n",name);
		out.printf("Parameter count: %d\n",parameterCount);
		out.printf("Maximum stack size: %d\n", stackSize);
		
		out.println();
		out.println("Constants:");
		for (int i = 0; i < constants.length; ++i) {
			out.printf(" %d:\t%.6f\n", i, constants[i]);
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
				case ByteCodes.CALL:
					i+=1;
					out.print(" "+instructions[i]);
					break;
				case ByteCodes.RET:
					i+=1;
					out.print(" "+instructions[i]);
					out.println();
					break;
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
