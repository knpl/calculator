package com.knpl.calc.util;

import java.util.ArrayList;
import java.util.List;

import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.nodes.numbers.RealDouble;

public class NumComputer {

	private final ArrayList<Num> mem;
	private Program program;
	
	public NumComputer(Program program) {
		mem = new ArrayList<Num>();
		this.program = program;
	}
	
	public Num execute(List<Num> input) throws Exception {
		if (program.getParameterCount() != input.size()) {
			throw new Exception("Argument mismatch: Program requires "+
				program.getParameterCount()+" parameters. Given: "+input.size());
		}
		
		mem.clear();
		mem.ensureCapacity(program.getParameterCount() + program.getStackSize());
		for (Num num : input) {
			mem.add(num);
		}
		
		eval(0, 0);
		Num result = mem.remove(mem.size() - 1);
		if (mem.size() != 0) {
			throw new Exception("Stack not empty");
		}
		
		return result;
	}
	
	private void eval(int pc, int bp) throws Exception {
		byte[] instrs = program.getInstructions();
		Num[] constants = program.getConstants();
		short[] offsets = program.getOffsets();
		int tos;
		
		while (true) {
			tos = mem.size() - 1;
			switch (instrs[pc]) {
			case ByteCodes.MINUS:
				mem.get(tos).neg();
				break;
			case ByteCodes.INC:
				mem.get(tos).add(new RealDouble(1));
				break;
			case ByteCodes.DEC:
				mem.get(tos).sub(new RealDouble(1));
				break;
			case ByteCodes.D2R:
				mem.get(tos).mul(new RealDouble(Math.PI/180.0));
				break;
			case ByteCodes.ADD:
				mem.get(tos-1).add(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.SUB:
				mem.get(tos-1).sub(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.MUL:
				mem.get(tos-1).mul(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.DIV:
				mem.get(tos-1).div(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.POW:
				mem.get(tos-1).pow(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.ABS:
				mem.get(tos).abs();
				break;
			case ByteCodes.MIN:
				mem.get(tos-1).min(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.MAX:
				mem.get(tos-1).max(mem.get(tos));
				mem.remove(tos);
				break;
			case ByteCodes.FLOOR:
				mem.get(tos).floor();
				break;
			case ByteCodes.CEIL:
				mem.get(tos).ceil();
				break;
			case ByteCodes.SQRT:
				mem.get(tos).sqrt();
				break;
			case ByteCodes.EXP:
				mem.get(tos).exp();
				break;
			case ByteCodes.LOG:
				mem.get(tos).log();
				break;
			case ByteCodes.SINH:
				mem.get(tos).sinh();
				break;
			case ByteCodes.COSH:
				mem.get(tos).cosh();
				break;
			case ByteCodes.TANH:
				mem.get(tos).tanh();
				break;
			case ByteCodes.SIN:
				mem.get(tos).sin();
				break;
			case ByteCodes.COS:
				mem.get(tos).cos();
				break;
			case ByteCodes.TAN:
				mem.get(tos).tan();
				break;
			case ByteCodes.ASIN:
				mem.get(tos).asin();
				break;
			case ByteCodes.ACOS:
				mem.get(tos).acos();
				break;
			case ByteCodes.ATAN:
				mem.get(tos).atan();
				break;
			case ByteCodes.ERF:
				mem.get(tos).erf();
				break;
			case ByteCodes.GAMMA:
				mem.get(tos).gamma();
				break;
			case ByteCodes.LOGGAMMA:
				mem.get(tos).loggamma();
				break;
			case ByteCodes.RE:
				mem.get(tos).re();
				break;
			case ByteCodes.IM:
				mem.get(tos).im();
				break;
			case ByteCodes.ARG:
				mem.get(tos).arg();
				break;
			case ByteCodes.MOD:
				mem.get(tos).mod();
				break;
			case ByteCodes.CONJ:
				mem.get(tos).conj();
				break;
			case ByteCodes.LOADC:
				pc += 1;
				mem.add(constants[instrs[pc]].copy());
				break;
			case ByteCodes.POP:
				mem.remove(tos);
				break;
			case ByteCodes.LOADA:
				pc += 1;
				mem.add(mem.get(bp + instrs[pc]).copy());
				break;
			case ByteCodes.CALL:
				eval(offsets[instrs[pc+1]], tos - instrs[pc+2] + 1);
				pc += 2;
				break;
			case ByteCodes.RET:
				mem.set(bp, mem.get(tos));
				for (int i = tos; i > bp; i--) {
					mem.remove(i);
				}
				return;
			default:
				throw new Exception("Illegal instruction: at &instr["+pc+"] : "+instrs[pc]);
			}
			pc += 1;
		}
	}
}
