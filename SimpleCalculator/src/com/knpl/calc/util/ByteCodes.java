package com.knpl.calc.util;

public class ByteCodes {
	public static final int
		MINUS = 0x01,
		ADD = 0x02,
		SUB = 0x03,
		MUL = 0x04,
		DIV = 0x05,
		POW = 0x06,
		
		ABS = 0x07,
		MIN = 0x08,
		MAX = 0x09,
		SQRT = 0x0a,
		
		EXP = 0x0b,
		LOG = 0x0c,
		
		SINH = 0x0d,
		COSH = 0x0e,
		
		SIN = 0x0f,
		COS = 0x10,
		TAN = 0x11,
		
		ASIN = 0x12,
		ACOS = 0x13,
		ATAN = 0x14,
		
		LOADA = 0x15,
		POP = 0x16,
		LOADC = 0x17,
		CALL = 0x18,
		RET = 0x19,
		
		FLOOR = 0x1a,
		CEIL = 0x1b,
		TANH = 0x1c,
		
		ERF = 0x1d,
		GAMMA = 0x1e,
		LOGGAMMA = 0x1f,
		
		D2R = 0x21,
		INC = 0x22,
		DEC = 0x23,
	
		MOD = 0x24;
	
		public static String toString(byte code) {
			String s;
			switch (code) {
				case MINUS:
					s = "MINUS";
					break;
				case ADD:
					s = "ADD";
					break;
				case SUB:
					s = "SUB";
					break;
				case MUL:
					s = "MUL";
					break;
				case DIV:
					s = "DIV";
					break;
				case POW:
					s = "POW";
					break;
				case ABS:
					s = "ABS";
					break;
				case MIN:
					s = "MIN";
					break;
				case MAX:
					s = "MAX";
					break;
				case SQRT:
					s = "SQRT";
					break;
				case EXP:
					s = "EXP";
					break;
				case LOG:
					s = "LOG";
					break;
				case SINH:
					s = "SINH";
					break;
				case COSH:
					s = "COSH";
					break;
				case SIN:
					s = "SIN";
					break;
				case COS:
					s = "COS";
					break;
				case TAN:
					s = "TAN";
					break;
				case ASIN:
					s = "ASIN";
					break;
				case ACOS:
					s = "ACOS";
					break;
				case ATAN:
					s = "ATAN";
					break;
				case LOADA:
					s = "LOADA";
					break;
				case POP:
					s = "POP";
					break;
				case LOADC:
					s = "LOADC";
					break;
				case CALL:
					s = "CALL";
					break;
				case RET:
					s = "RET";
					break;
				
				case FLOOR:
					s = "FLOOR";
					break;
				case CEIL:
					s = "CEIL";
					break;
				case TANH:
					s = "TANH";
					break;
				case ERF:
					s = "ERF";
					break;
				case GAMMA:
					s = "GAMMA";
					break;
				case LOGGAMMA:
					s = "LOGGAMMA";
					break;
				case D2R:
					s = "D2R";
					break;
				case INC:
					s = "INC";
					break;
				case DEC:
					s = "DEC";
					break;
				case MOD:
					s = "MOD";
					break;
				default:
					s = "Invalid instruction";
			}
			return s;
		}
	
		
}
