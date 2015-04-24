package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.util.MyNumber;
import com.knpl.simplecalculator.util.RealDouble;
import com.knpl.simplecalculator.visitors.Visitor;

public class BuiltinConstDefs {
	
	public static final ConstDef[] builtinConstDefs = {
		new PiDef(), new EDef(), new IDef()
	};
	
	/* Constants */
	public static class PiDef extends ConstDef {

		public static final String NAME = "\u03C0";
		public static final String DESCRIPTION = "The ratio of a " +
						  "circle's circumference to its diameter.";
		
		public PiDef() {
			super(NAME, DESCRIPTION);
		}
		
		@Override
		public MyNumber getNumber() {
			return new RealDouble(Math.PI);
		}

		@Override
		public double getDouble() {
			return Math.PI;
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.PI, 0);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class EDef extends ConstDef {
		public static final String NAME = "e";
		public static final String DESCRIPTION = "Base of the natural logarithm.";
		
		public EDef() {
			super(NAME, DESCRIPTION);
		}
		
		@Override
		public MyNumber getNumber() {
			return new RealDouble(Math.E);
		}

		@Override
		public double getDouble() {
			return Math.E;
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.E, 0);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class IDef extends ConstDef {
		public static final String NAME = "i";
		public static final String DESCRIPTION = "The imaginary unit.";

		public IDef() {
			super(NAME, DESCRIPTION);
		}
		
		@Override
		public MyNumber getNumber() {
			return new Complex(0, 1);
		}

		@Override
		public double getDouble() {
			return Double.NaN;
		}

		@Override
		public Complex getComplex() {
			return new Complex(0, 1);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
