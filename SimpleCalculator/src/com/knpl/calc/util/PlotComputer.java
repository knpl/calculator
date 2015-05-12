package com.knpl.calc.util;

import java.io.Serializable;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;

import com.knpl.calc.nodes.numbers.Num;

public class PlotComputer implements Serializable {

	private static final long serialVersionUID = -6393208190823210598L;
	private Program program;
	
	public PlotComputer(Program program) {
		this.program = program;
	}
	
	public void execute(float[] dst, int dindex, int dstep,
			 			float[] src, int sindex, int sstep)
	{	
		int n = (src.length / sstep);
		int paramcnt = program.getParameterCount();
		float[] stacks = new float[n * program.getStackSize()];
		
		for (int i = 0; i < paramcnt; ++i) {
			int row = i*n;
			for (int j = 0; j < n; ++j) {
				stacks[row + j] = src[j*sstep + sindex];
			}
		}
		simd(stacks, n, 0, paramcnt, 0);
		
		for (int j = 0; j < n; ++j) {
			dst[j*dstep + dindex] = stacks[j];
		}
	}
	
	private void simdMinus(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = -stacks[i];
		}
	}
	
	private void simdInc(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i]++;
		}
	}
	
	private void simdDec(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i]--;
		}
	}
	
	private void simdD2R(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] *= (float)Math.PI/180;
		}
	}
	
	private void simdAdd(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] += stacks[i + n];
		}
	}
	
	private void simdSub(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] -= stacks[i + n];
		}
	}
	
	private void simdMul(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] *= stacks[i + n];
		}
	}
	
	private void simdDiv(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] /= stacks[i + n];
		}
	}
	
	private void simdPow(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.pow(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdAbs(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = Math.abs(stacks[i]);
		}
	}
	
	private void simdMin(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = Math.min(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdMax(float[] stacks, int n, int sp) {
		int dst = n * (sp - 2);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = Math.max(stacks[i], stacks[i + n]);
		}
	}
	
	private void simdFloor(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.floor(stacks[i]);
		}
	}
	
	private void simdCeil(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.ceil(stacks[i]);
		}
	}
	
	private void simdSqrt(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.sqrt(stacks[i]);
		}
	}
	
	private void simdExp(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.exp(stacks[i]);
		}
	}
	
	private void simdLog(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.log(stacks[i]);
		}
	}
	
	private void simdSinh(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.sinh(stacks[i]);
		}
	}
	
	private void simdCosh(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.cosh(stacks[i]);
		}
	}
	
	private void simdTanh(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.tanh(stacks[i]);
		}
	}
	
	private void simdSin(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.sin(stacks[i]);
		}
	}
	
	private void simdCos(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.cos(stacks[i]);
		}
	}
	
	private void simdTan(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.tan(stacks[i]);
		}
	}
	
	private void simdAsin(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.asin(stacks[i]);
		}
	}
	
	private void simdAcos(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.acos(stacks[i]);
		}
	}
	
	private void simdAtan(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Math.atan(stacks[i]);
		}
	}
	
	private void simdErf(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			try {
				stacks[i] = (float) Erf.erf(stacks[i]);
			}
			catch (Exception ex) {
				stacks[i] = Float.NaN;
			}
		}
	}
	
	private void simdGamma(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Gamma.gamma(stacks[i]);
		}
	}
	
	private void simdLogGamma(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = (float)Gamma.logGamma(stacks[i]);
		}
	}
	
	private void simdIm(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			stacks[i] = 0f;
		}
	}
	
	private void simdArg(float[] stacks, int n, int sp) {
		int dst = n * (sp - 1);
		for (int i = dst; i < dst + n; i++) {
			if (stacks[i] == 0f)
				stacks[i] = Float.NaN;
			else
				stacks[i] = (stacks[i] < 0) ? (float)Math.PI : 0f;
		}
	}
	
	private void simdLoadc(float[] stacks, int n, int sp, float constant) {
		int index = n * sp;
		for (int i = index; i < index + n; i++) {
			stacks[i] = constant;
		}
	}
	
	private void simdLoada(float[] stacks, int n, int sp, int bp, int index) {
		int src = n * (bp + index);
		int dst = n * sp;
		
		for (int i = 0; i < n; i++) {
			stacks[dst] = stacks[src];
			dst++;
			src++;
		}
	}
	
	private void simdRet(float[] stacks, int n, int sp, int bp) {
		int src = n * (sp - 1);
		int dst = n * bp;
		
		for (int i = 0; i < n; i++) {
			stacks[dst] = stacks[src];
			dst++;
			src++;
		}
	}
	
	private void simd(float[] stacks, int n, int pc, int sp, int bp) {
		byte[] instrs = program.getInstructions();
		short[] offsets = program.getOffsets();
		Num[] constants = program.getConstants();
		
		while (true) {
			switch (instrs[pc]) {
			case ByteCodes.MINUS:
				simdMinus(stacks, n, sp);
				break;
			case ByteCodes.INC:
				simdInc(stacks, n, sp);
				break;
			case ByteCodes.DEC:
				simdDec(stacks, n, sp);
				break;
			case ByteCodes.D2R:
				simdD2R(stacks, n, sp);
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
			case ByteCodes.FLOOR:
				simdFloor(stacks, n, sp);
				break;
			case ByteCodes.CEIL:
				simdCeil(stacks, n, sp);
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
			case ByteCodes.TANH:
				simdTanh(stacks, n, sp);
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
			case ByteCodes.ERF:
				simdErf(stacks, n, sp);
				break;
			case ByteCodes.GAMMA:
				simdGamma(stacks, n, sp);
				break;
			case ByteCodes.LOGGAMMA:
				simdLogGamma(stacks, n, sp);
				break;
			case ByteCodes.RE:
				break;
			case ByteCodes.IM:
				simdIm(stacks, n, sp);
				break;
			case ByteCodes.ARG:
				simdArg(stacks, n, sp);
				break;
			case ByteCodes.MOD:
				simdAbs(stacks, n, sp);
				break;
			case ByteCodes.CONJ:
				break;
			case ByteCodes.LOADC:
				pc++;
				simdLoadc(stacks, n, sp, constants[instrs[pc]].toFloat());
				sp++;
				break;
			case ByteCodes.POP:
				sp -= 1;
				break;
			case ByteCodes.LOADA:
				pc++;
				simdLoada(stacks, n, sp, bp, instrs[pc]);
				sp++;
				break;
			case ByteCodes.CALL:
				int nargs = instrs[pc+2];
				simd(stacks, n, offsets[instrs[pc+1]], sp, sp - nargs);
				pc += 2;
				sp = sp - nargs + 1;
				break;
			case ByteCodes.RET:
				simdRet(stacks, n, sp, bp);
				return;
			default:
				simdLoadc(stacks, n, sp, Float.NaN);
				sp++;
				simdRet(stacks, n, sp, bp);
				return;
			}
			pc++;
		}
	}
}
