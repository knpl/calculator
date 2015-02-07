package com.knpl.simplecalculator.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Program implements Serializable {
	
	private static final long serialVersionUID = -2503458081984075342L;
	
	private String name;
	private ArrayList<Double> constants;
	private ArrayList<Integer> offsets;
	private byte[] instructions;
	private int parameterCount;
	private ArrayList<Double> stack;
	
	public Program(String name, byte[] instructions, ArrayList<Double> constants, ArrayList<Integer> offsets, int parameterCount) {
		this.name = name;
		this.constants = constants;
		this.offsets = offsets;
		this.instructions = instructions;
		this.parameterCount = parameterCount;
		this.stack = new ArrayList<Double>();
	}
	
	public String getName() {
		return name;
	}
	
	public int getParameterCount() {
		return parameterCount;
	}
	
	public double evaluate(List<Double> arguments) throws Exception {
		double result;
		
		if (arguments.size() != parameterCount) {
			throw new Exception("program: "+name+" called with "+arguments.size()+
									   " arguments. (Should be "+parameterCount+")");
		}
		
		for (int i = arguments.size() - 1; i >= 0; --i) {
			push(arguments.get(i));
		}
		
		evaluateR(0, stack.size());
		
		result = stack.get(stack.size()-1);
		
		stack.clear();
		
		return result;
	}
	
	public double evaluate(double arg) throws Exception {
		if (parameterCount != 1) {
			throw new Exception("program "+name+" called with 1 argument. "
								+parameterCount+" arguments required");
		}
		double result;
		push(arg);
		evaluateR(0, stack.size());
		result = stack.get(stack.size()-1);
		stack.clear();
		return result;
	}
	
	public void evaluate(float[] dst, int dindex, int dstep,
						 float[] src, int sindex, int sstep)
	{
		if (sstep < parameterCount) {
			sstep = parameterCount;
		}
		if (dstep <= dindex) {
			dindex %= dstep;
		}
		if (sstep <= sindex) {
			sindex %= sstep;
		}
		
		int dstlen = (dst.length / dstep);
		int srclen = (src.length / sstep);
		int len = Math.min(srclen, dstlen);
		
		int si = sindex,
			di = dindex;
		for (int i = 0; i < len; ++i) {
			for (int j = 0; j < parameterCount; ++j) {
				push((double)src[si + j]);
			}
			evaluateR(0, stack.size());
			dst[di] = (float)pop();
			stack.clear();
			
			di += dstep;
			si += sstep;
		}
	}
	
	private void push(double a) {
		stack.add(a);
	}
	
	private double pop() {
		int lastIndex = stack.size() - 1;
		stack.get(lastIndex);
		return stack.remove(lastIndex);
	}
	
	private void sub(int a) {
		int lastIndex = stack.size() - 1;
		
		for (int i = 0; i < a; ++i) {
			stack.remove(lastIndex);
			lastIndex -= 1;
		}
	}
	
	private void evaluateR(int pc, int sp) {
		int index;
		double a, b;
		for (int i=pc; ; ++i) {
			switch (instructions[i]) {
				case ByteCodes.MINUS:
					push(-pop());
					break;
					
				case ByteCodes.ADD:
					b = pop(); a = pop();
					push(a+b);
					break;
				
				case ByteCodes.SUB:
					b = pop(); a = pop();
					push(a-b);
					break;
					
				case ByteCodes.MUL:
					b = pop(); a = pop();
					push(a*b);
					break;
					
				case ByteCodes.DIV:
					b = pop(); a = pop();
					push(a/b);
					break;
					
				case ByteCodes.POW:
					b = pop(); a = pop();
					push(Math.pow(a, b));
					break;
					
				case ByteCodes.ABS:
					push(Math.abs(pop()));
					break;
					
				case ByteCodes.MIN:
					b = pop(); a = pop();
					push(Math.min(a, b));
					break;
					
				case ByteCodes.MAX:
					b = pop(); a = pop();
					push(Math.max(a,b));
					break;
					
				case ByteCodes.SQRT:
					push(Math.sqrt(pop()));
					break;
					
				case ByteCodes.EXP:
					push(Math.exp(pop()));
					break;
					
				case ByteCodes.LOG:
					push(Math.log(pop()));
					break;
					
				case ByteCodes.SINH:
					push(Math.sinh(pop()));
					break;
					
				case ByteCodes.COSH:
					push(Math.cosh(pop()));
					break;
					
				case ByteCodes.SIN:
					push(Math.sin(pop()));
					break;
					
				case ByteCodes.COS:
					push(Math.cos(pop()));
					break;
					
				case ByteCodes.TAN:
					push(Math.tan(pop()));
					break;
					
				case ByteCodes.ASIN:
					push(Math.asin(pop()));
					break;
					
				case ByteCodes.ACOS:
					push(Math.acos(pop()));
					break;
					
				case ByteCodes.ATAN:
					push(Math.atan(pop()));
					break;
					
				case ByteCodes.LOADC:
					i+=1;
					index = instructions[i];
					push(constants.get(index));
					break;
				
				case ByteCodes.POP:
					pop();
					break;
					
				case ByteCodes.LOADA:
					i+=1;
					index = sp-(instructions[i]+1);
					push(stack.get(index));
					break;
					
				case ByteCodes.CALL:
					i+=1;
					index = instructions[i];
					evaluateR(offsets.get(index), stack.size());
					break;
				
				case ByteCodes.RET:
					i+=1;
					a = pop();
					index = instructions[i];
					sub(index);
					push(a);
					return;
					
				default:
			}
		}
	}
	
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		
		out.printf("Program name: %s\n",name);
		out.printf("Parameter count: %d\n",parameterCount);
		
		out.println();
		out.println("Constants:");
		for (int i = 0; i < constants.size(); ++i) {
			out.printf(" %d:\t%.6f\n", i, constants.get(i));
		}
		
		out.println();
		out.println("Offsets:");
		for (int i = 0; i < offsets.size(); ++i) {
			out.printf(" %d:\t%d\n", i, offsets.get(i));
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
